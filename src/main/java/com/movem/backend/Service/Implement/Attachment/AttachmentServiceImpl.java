package com.movem.backend.Service.Implement.Attachment;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Entity.Attachment.Attachment;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.AttachmentRepository.AttachmentRepository;
import com.movem.backend.Service.AttachmentService.AttachmentService;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final CurrentUserService currentUserService;
    private final GcsFileStorageService gcsFileStorageService;

    @Override
    public AttachmentResponse upload(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty.");
        }

        User currentUser = currentUserService.getCurrentUser();

        try {
            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null || originalFileName.isBlank()) {
                originalFileName = "file";
            }

            String objectKey = gcsFileStorageService.upload("attachments", file);
            String storedFileName = objectKey.substring(objectKey.lastIndexOf("/") + 1);

            Attachment attachment = Attachment.builder()
                            .originalFileName(originalFileName)
                            .storedFileName(storedFileName)
                            .fileType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                            .fileSize(file.getSize())
                            /*
                             * IMPORTANT:
                             * Store the GCS object key,
                             * NOT a local filesystem path.
                             */
                            .filePath(objectKey)
                            .uploadedBy(currentUser)
                            .createdAt(LocalDateTime.now())
                            .build();

            return toResponse(attachmentRepository.save(attachment));

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Google Cloud Storage.", e);
        }
    }

    @Override
    public List<AttachmentResponse> getMyAttachments() {
        User currentUser = currentUserService.getCurrentUser();

        return attachmentRepository
                .findByUploadedByAndDeletedAtIsNull(currentUser)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AttachmentResponse getAttachment(Long attachmentId) {
        User currentUser = currentUserService.getCurrentUser();

        Attachment attachment = attachmentRepository.findByIdAndUploadedByAndDeletedAtIsNull(attachmentId, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found."));

        return toResponse(attachment);
    }
    @Override
    public void delete(Long attachmentId) {
        User currentUser = currentUserService.getCurrentUser();

        Attachment attachment = attachmentRepository
                        .findByIdAndUploadedByAndDeletedAtIsNull(attachmentId, currentUser)
                        .orElseThrow(() -> new ResourceNotFoundException("Attachment not found."));


        gcsFileStorageService.delete(attachment.getFilePath());

        attachment.setDeletedAt(LocalDateTime.now());

        attachmentRepository.save(attachment);
    }

    private AttachmentResponse toResponse(Attachment attachment) {
        return AttachmentResponse.builder()
                .id(attachment.getId())
                .originalFileName(attachment.getOriginalFileName())
                .fileType(attachment.getFileType())
                .fileSize(attachment.getFileSize())
                .filePath(attachment.getFilePath())
                .uploadedBy(attachment.getUploadedBy().getId())
                .createdAt(attachment.getCreatedAt())
                .url(gcsFileStorageService.signedUrl(attachment.getFilePath()).toString())
                .build();
    }

    @Override
    public ResponseEntity<Resource> view(Long attachmentId) {
        User currentUser = currentUserService.getCurrentUser();

        Attachment attachment = attachmentRepository
                        .findByIdAndUploadedByAndDeletedAtIsNull(attachmentId, currentUser)
                        .orElseThrow(() -> new ResourceNotFoundException("Attachment not found."));

        try {
            byte[] fileBytes = gcsFileStorageService.download(attachment.getFilePath());
            Resource resource = new ByteArrayResource(fileBytes);
            MediaType mediaType;
            try {
                mediaType = MediaType.parseMediaType(attachment.getFileType());

            } catch (Exception e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .contentLength(fileBytes.length)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve attachment from Google Cloud Storage.", e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(Long attachmentId) {
        User currentUser = currentUserService.getCurrentUser();

        Attachment attachment = attachmentRepository
                        .findByIdAndUploadedByAndDeletedAtIsNull(attachmentId, currentUser)
                        .orElseThrow(() -> new ResourceNotFoundException("Attachment not found."));

        try {
            byte[] fileBytes = gcsFileStorageService.download(attachment.getFilePath());
            Resource resource = new ByteArrayResource(fileBytes);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileBytes.length)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getOriginalFileName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Failed to download attachment from Google Cloud Storage.", e);
        }
    }
}
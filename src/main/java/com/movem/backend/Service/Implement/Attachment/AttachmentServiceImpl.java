package com.movem.backend.Service.Implement.Attachment;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Entity.Attachment.Attachment;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.AttachmentRepository.AttachmentRepository;
import com.movem.backend.Service.AttachmentService.AttachmentService;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final CurrentUserService currentUserService;

    private final Path uploadDirectory =
            Paths.get("uploads/attachments");

    @Override
    public AttachmentResponse upload(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "File cannot be empty."
            );
        }

        User currentUser =
                currentUserService.getCurrentUser();

        try {

            Files.createDirectories(uploadDirectory);

            String originalFileName =
                    file.getOriginalFilename();

            String storedFileName =
                    UUID.randomUUID()
                            + "_"
                            + originalFileName;

            Path target =
                    uploadDirectory.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            Attachment attachment =
                    Attachment.builder()
                            .originalFileName(originalFileName)
                            .storedFileName(storedFileName)
                            .fileType(file.getContentType())
                            .fileSize(file.getSize())
                            .filePath(target.toString())
                            .uploadedBy(currentUser)
                            .createdAt(LocalDateTime.now())
                            .build();

            return toResponse(
                    attachmentRepository.save(attachment)
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to store file.",
                    e
            );
        }
    }

    @Override
    public List<AttachmentResponse> getMyAttachments() {

        User currentUser =
                currentUserService.getCurrentUser();

        return attachmentRepository
                .findByUploadedByAndDeletedAtIsNull(currentUser)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AttachmentResponse getAttachment(
            Long attachmentId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Attachment attachment =
                attachmentRepository
                        .findByIdAndUploadedByAndDeletedAtIsNull(
                                attachmentId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attachment not found."
                                )
                        );

        return toResponse(attachment);
    }

    @Override
    public void delete(Long attachmentId) {

        User currentUser =
                currentUserService.getCurrentUser();

        Attachment attachment =
                attachmentRepository
                        .findByIdAndUploadedByAndDeletedAtIsNull(
                                attachmentId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attachment not found."
                                )
                        );

        attachment.setDeletedAt(
                LocalDateTime.now()
        );

        attachmentRepository.save(attachment);
    }

    private AttachmentResponse toResponse(
            Attachment attachment
    ) {

        return AttachmentResponse.builder()
                .id(attachment.getId())
                .originalFileName(
                        attachment.getOriginalFileName()
                )
                .fileType(
                        attachment.getFileType()
                )
                .fileSize(
                        attachment.getFileSize()
                )
                .filePath(
                        attachment.getFilePath()
                )
                .uploadedBy(
                        attachment.getUploadedBy().getId()
                )
                .createdAt(
                        attachment.getCreatedAt()
                )
                .build();
    }

    @Override
    public ResponseEntity<Resource> view(Long attachmentId) {

        User currentUser =
                currentUserService.getCurrentUser();

        Attachment attachment =
                attachmentRepository
                        .findByIdAndUploadedByAndDeletedAtIsNull(
                                attachmentId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attachment not found."
                                )
                        );

        Resource resource =
                new FileSystemResource(
                        attachment.getFilePath()
                );

        if (!resource.exists()) {
            throw new ResourceNotFoundException(
                    "Attachment file not found."
            );
        }

        MediaType mediaType;

        try {
            mediaType = MediaType.parseMediaType(
                    attachment.getFileType()
            );
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    @Override
    public ResponseEntity<Resource> download(Long attachmentId) {

        User currentUser =
                currentUserService.getCurrentUser();

        Attachment attachment =
                attachmentRepository
                        .findByIdAndUploadedByAndDeletedAtIsNull(
                                attachmentId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attachment not found."
                                )
                        );

        Resource resource =
                new FileSystemResource(
                        attachment.getFilePath()
                );

        if (!resource.exists()) {
            throw new ResourceNotFoundException(
                    "Attachment file not found."
            );
        }

        return ResponseEntity.ok()
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                attachment.getOriginalFileName() +
                                "\""
                )
                .body(resource);
    }
}
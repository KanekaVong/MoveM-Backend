package com.movem.backend.shared.attachment.services.impl;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.attachment.entities.Attachment;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.challenges.entities.GroupFitnessChallenge;
import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.fitness.club.entities.FitnessClubMember;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.fitness.challenges.repositories.GroupFitnessChallengeRepository;
import com.movem.backend.fitness.club.repositories.FitnessClubMemberRepository;
import com.movem.backend.fitness.club.repositories.FitnessClubRepository;
import com.movem.backend.shared.attachment.services.AttachmentService;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.commons.enums.Fitness.FitnessAttachmentType;
import com.movem.backend.commons.enums.Fitness.FitnessClubRole;
import com.movem.backend.shared.attachment.services.GcsFileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
    private final FitnessClubRepository fitnessClubRepository;
    private final FitnessClubMemberRepository fitnessClubMemberRepository;
    private final GroupFitnessChallengeRepository groupFitnessChallengeRepository;

    @Override
    @Transactional
    public AttachmentResponse uploadClubProfile(Integer clubId, MultipartFile file) {
        FitnessClub club = getClub(clubId);
        requireClubOwnerOrAdmin(club);

        attachmentRepository.findByFitnessClubAndAttachmentTypeAndDeletedAtIsNull(club,FitnessAttachmentType.CLUB_PROFILE)
                .forEach(existing -> {
                    gcsFileStorageService.delete(existing.getFilePath());
                    existing.setDeletedAt(LocalDateTime.now());
                    attachmentRepository.save(existing);
                });

        AttachmentResponse uploaded = upload(file);

        Attachment attachment = attachmentRepository
                .findById(uploaded.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found."));

        attachment.setFitnessClub(club);
        attachment.setAttachmentType(FitnessAttachmentType.CLUB_PROFILE);

        club.setUpdatedAt(LocalDateTime.now());
        fitnessClubRepository.save(club);

        return toResponse(attachmentRepository.save(attachment));
    }

    @Override
    @Transactional
    public AttachmentResponse uploadClubCover(Integer clubId, MultipartFile file) {
        FitnessClub club = getClub(clubId);
        requireClubOwnerOrAdmin(club);

        attachmentRepository
                .findByFitnessClubAndAttachmentTypeAndDeletedAtIsNull(
                        club,
                        FitnessAttachmentType.CLUB_COVER
                )
                .forEach(existing -> {
                    gcsFileStorageService.delete(existing.getFilePath());
                    existing.setDeletedAt(LocalDateTime.now());
                    attachmentRepository.save(existing);
                });

        AttachmentResponse uploaded = upload(file);

        Attachment attachment = attachmentRepository
                .findById(uploaded.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found."));

        attachment.setFitnessClub(club);
        attachment.setAttachmentType(FitnessAttachmentType.CLUB_COVER);

        club.setUpdatedAt(LocalDateTime.now());
        fitnessClubRepository.save(club);

        return toResponse(attachmentRepository.save(attachment));
    }

    @Override
    @Transactional
    public List<AttachmentResponse> getClubAttachments(Integer clubId) {
        FitnessClub club = getClub(clubId);

        return attachmentRepository
                .findByFitnessClubAndDeletedAtIsNull(club)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AttachmentResponse uploadChallengeAttachment(Integer challengeId, MultipartFile file) {
        GroupFitnessChallenge challenge = groupFitnessChallengeRepository
                .findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Group fitness challenge not found."));

        requireChallengeAccess(challenge);

        attachmentRepository.findByGroupFitnessChallengeAndAttachmentTypeAndDeletedAtIsNull(challenge, FitnessAttachmentType.CHALLENGE_IMAGE)
                .forEach(existing -> {existing.setDeletedAt(LocalDateTime.now());attachmentRepository.save(existing);});

        AttachmentResponse uploaded = upload(file);

        Attachment attachment = attachmentRepository
                .findById(uploaded.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found."));

        attachment.setGroupFitnessChallenge(challenge);
        attachment.setAttachmentType(FitnessAttachmentType.CHALLENGE_IMAGE);

        return toResponse(attachmentRepository.save(attachment));
    }

    @Override
    @Transactional
    public List<AttachmentResponse> getChallengeAttachments(Integer challengeId) {
        GroupFitnessChallenge challenge = groupFitnessChallengeRepository
                .findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Group fitness challenge not found."));

        return attachmentRepository
                .findByGroupFitnessChallengeAndAttachmentTypeAndDeletedAtIsNull(
                        challenge,
                        FitnessAttachmentType.CHALLENGE_IMAGE
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

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
    public void delete(Long attachmentId) {
        User currentUser = currentUserService.getCurrentUser();

        Attachment attachment = attachmentRepository
                .findByIdAndUploadedByAndDeletedAtIsNull(attachmentId, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found."));
        gcsFileStorageService.delete(attachment.getFilePath());
        attachment.setDeletedAt(LocalDateTime.now());
        attachmentRepository.save(attachment);
    }

    @Override
    public AttachmentResponse toResponse(Attachment attachment) {
        return AttachmentResponse.builder()
                .id(attachment.getId())
                .originalFileName(attachment.getOriginalFileName())
                .fileType(attachment.getFileType())
                .fileSize(attachment.getFileSize())
                .filePath(attachment.getFilePath())
                .uploadedBy(attachment.getUploadedBy().getId())
                .createdAt(attachment.getCreatedAt())
                .url(gcsFileStorageService.publicUrl(attachment.getFilePath()))
                .build();
    }

    @Override
    public ResponseEntity<Resource> view(Long attachmentId) {
        User currentUser = currentUserService.getCurrentUser();

        Attachment attachment = attachmentRepository.findByIdAndUploadedByAndDeletedAtIsNull(attachmentId, currentUser).orElseThrow(() -> new ResourceNotFoundException("Attachment not found."));

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

    private FitnessClub getClub(Integer clubId) {
        return fitnessClubRepository.findById(clubId).orElseThrow(() -> new ResourceNotFoundException("Fitness club not found."));
    }

    private void requireClubOwnerOrAdmin(FitnessClub club) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessClubMember member = fitnessClubMemberRepository.findByFitnessClubAndUser(club, currentUser).orElseThrow(() -> new AccessDeniedException("You are not a member of this fitness club."));

        if (member.getRole() != FitnessClubRole.OWNER && member.getRole() != FitnessClubRole.ADMIN) {
            throw new AccessDeniedException("Only club owner or admin can manage club attachments.");
        }
    }

    private void requireChallengeAccess(GroupFitnessChallenge challenge) {
        User currentUser = currentUserService.getCurrentUser();
        if (challenge.getCreatedBy().getId().equals(currentUser.getId())) {
            return;
        }

        FitnessClub club = challenge.getFitnessClub();
        FitnessClubMember member = fitnessClubMemberRepository.findByFitnessClubAndUser(club, currentUser).orElseThrow(() -> new AccessDeniedException("You are not a member of this fitness club."));

        if (member.getRole() != FitnessClubRole.OWNER && member.getRole() != FitnessClubRole.ADMIN) {
            throw new AccessDeniedException("Only challenge creator, club owner or admin can manage the challenge image.");
        }
    }

}
package com.movem.backend.Service.Implement.Attachment;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Entity.Attachment.Attachment;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.AttachmentRepository.AttachmentRepository;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutSessionRepository;
import com.movem.backend.Service.AttachmentService.AttachmentService;
import com.movem.backend.Service.AttachmentService.FitnessWorkoutAttachmentService;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FitnessWorkoutAttachmentServiceImpl
        implements FitnessWorkoutAttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final AttachmentService attachmentService;
    private final CurrentUserService currentUserService;

    @Override
    public AttachmentResponse upload(
            Integer sessionId,
            MultipartFile file
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUser(
                                sessionId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (
                session.getStatus()
                        != FitnessWorkoutStatus.COMPLETED
        ) {
            throw new IllegalArgumentException(
                    "Attachments can only be added to completed workouts."
            );
        }

        if (!Boolean.TRUE.equals(session.getIsShared())) {
            throw new IllegalArgumentException(
                    "You must share the workout before adding attachments."
            );
        }

        /*
         * Use the existing generic attachment uploader.
         */
        AttachmentResponse uploaded =
                attachmentService.upload(file);

        Attachment attachment =
                attachmentRepository
                        .findById(uploaded.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Uploaded attachment not found."
                                )
                        );

        attachment.setWorkoutSession(session);

        attachment.setCreatedAt(
                attachment.getCreatedAt() != null
                        ? attachment.getCreatedAt()
                        : LocalDateTime.now()
        );

        return toResponse(
                attachmentRepository.save(attachment)
        );
    }

    @Override
    public List<AttachmentResponse> getAttachments(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUser(
                                sessionId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        return attachmentRepository
                .findByWorkoutSessionAndDeletedAtIsNull(
                        session
                )
                .stream()
                .map(this::toResponse)
                .toList();
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
}
package com.movem.backend.Service.Implement.Attachment;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Entity.Attachment.Attachment;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.AttachmentRepository.AttachmentRepository;
import com.movem.backend.Repository.TripRepositories.TripRepository;
import com.movem.backend.Service.AttachmentService.AttachmentService;
import com.movem.backend.Service.AttachmentService.TripAttachmentService;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripAttachmentServiceImpl
        implements TripAttachmentService {

    private final TripRepository tripRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;
    private final CurrentUserService currentUserService;

    @Override
    public AttachmentResponse upload(
            String activityId,
            MultipartFile file
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Trip trip =
                tripRepository
                        .findById(activityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Trip not found."
                                )
                        );

        if (!trip.getActivity()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You can only attach files to your own trip."
            );
        }

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

        attachment.setTrip(trip);

        attachmentRepository.save(attachment);

        return uploaded;
    }

    @Override
    public List<AttachmentResponse> getAttachments(
            String activityId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Trip trip =
                tripRepository
                        .findById(activityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Trip not found."
                                )
                        );

        if (!trip.getActivity()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You can only view attachments from your own trip."
            );
        }

        return attachmentRepository
                .findByTripAndDeletedAtIsNull(trip)
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
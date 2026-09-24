package com.movem.backend.shared.attachment.services.impl;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.attachment.entities.Attachment;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.shared.attachment.services.GcsFileStorageService;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.trip.repositories.TripRepository;
import com.movem.backend.shared.attachment.services.AttachmentService;
import com.movem.backend.shared.attachment.services.TripAttachmentService;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.shared.activity.services.ActivityPermissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripAttachmentServiceImpl implements TripAttachmentService {

    private final TripRepository tripRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;
    private final CurrentUserService currentUserService;
    private final ActivityPermissionService activityPermissionService;
    private final GcsFileStorageService gcsFileStorageService;

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

        Attachment saved =
                attachmentRepository.save(attachment);

        return attachmentService.toResponse(saved);
    }

    @Override
    public List<AttachmentResponse> getAttachments(String activityId) {

        Trip trip = tripRepository.findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        Attachment coverPhoto = trip.getCoverPhoto();

        List<Attachment> attachments;

        if (coverPhoto == null) {
            attachments = attachmentRepository
                    .findByTripAndDeletedAtIsNull(trip);
        } else {
            attachments = attachmentRepository
                    .findByTripAndDeletedAtIsNullAndIdNot(
                            trip,
                            coverPhoto.getId()
                    );
        }

        return attachments.stream()
                .map(attachmentService::toResponse)
                .toList();
    }
    @Override
    @Transactional
    public AttachmentResponse uploadCoverPhoto(
            String activityId,
            MultipartFile file
    ) {
        Trip trip = tripRepository.findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        User currentUser = currentUserService.getCurrentUser();

        activityPermissionService.validateCanEditActivity(
                trip.getActivity(),
                currentUser
        );

        // Delete old cover
        Attachment oldCover = trip.getCoverPhoto();

        if (oldCover != null) {
            gcsFileStorageService.delete(oldCover.getFilePath());

            oldCover.setDeletedAt(LocalDateTime.now());
            attachmentRepository.save(oldCover);
        }

        // Upload new file
        AttachmentResponse uploaded = attachmentService.upload(file);

        Attachment attachment = attachmentRepository
                .findById(uploaded.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Attachment not found.")
                );

        attachment.setTrip(trip);

        Attachment saved = attachmentRepository.save(attachment);

        // Set as current cover
        trip.setCoverPhoto(saved);
        tripRepository.save(trip);

        return attachmentService.toResponse(saved);
    }
    @Override
    public AttachmentResponse getCoverPhoto(String activityId) {

        Trip trip = tripRepository.findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        Attachment coverPhoto = trip.getCoverPhoto();

        if (coverPhoto == null || coverPhoto.getDeletedAt() != null) {
            throw new ResourceNotFoundException(
                    "Trip cover photo not found."
            );
        }

        return attachmentService.toResponse(coverPhoto);
    }
    @Override
    @Transactional
    public void deleteCoverPhoto(String activityId) {

        Trip trip = tripRepository.findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        User currentUser = currentUserService.getCurrentUser();

        activityPermissionService.validateCanEditActivity(
                trip.getActivity(),
                currentUser
        );

        Attachment coverPhoto = trip.getCoverPhoto();

        if (coverPhoto == null) {
            throw new ResourceNotFoundException(
                    "Trip cover photo not found."
            );
        }

        gcsFileStorageService.delete(coverPhoto.getFilePath());

        coverPhoto.setDeletedAt(LocalDateTime.now());
        attachmentRepository.save(coverPhoto);

        trip.setCoverPhoto(null);
        tripRepository.save(trip);
    }
}
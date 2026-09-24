package com.movem.backend.trip.mappers.impl;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.trip.dtos.responses.TripResponse;
import com.movem.backend.trip.dtos.responses.TripSummaryResponse;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.BaseMapper.AbstractBaseMapper;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.trip.mappers.TripMapper;
import com.movem.backend.trip.mappers.TripStopMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripMapperImpl extends AbstractBaseMapper<Trip, TripResponse> implements TripMapper {
    private final TripStopMapper tripStopMapper;
    private final AttachmentRepository attachmentRepository;

    @Override
    public TripResponse toResponse(Trip trip) {
        if (trip == null) {
            return null;
        }
        Activity activity = trip.getActivity();
        if (activity == null) {
            return null;
        }
        return TripResponse.builder()
                .activityId(activity.getId())
                .activityName(activity.getActivityName())
                .description(activity.getDescription())
                .status(activity.getStatus())
                .startActivity(activity.getStartActivity())
                .deadline(activity.getDeadline())
                .locationName(activity.getLocationName())
                .locationAddress(activity.getLocationAddress())
                .lat(activity.getLat())
                .lng(activity.getLng())
                .googlePlaceId(activity.getGooglePlaceId())
                .destination(trip.getDestination())
                .stops(tripStopMapper.toResponseList(trip.getStops()))
                .coverPhoto(
                        trip.getCoverPhoto() != null
                                ? AttachmentResponse.builder()
                                .id(trip.getCoverPhoto().getId())
                                .originalFileName(trip.getCoverPhoto().getOriginalFileName())
                                .fileType(trip.getCoverPhoto().getFileType())
                                .fileSize(trip.getCoverPhoto().getFileSize())
                                .filePath(trip.getCoverPhoto().getFilePath())
                                .uploadedBy(trip.getCoverPhoto().getUploadedBy().getId())
                                .createdAt(trip.getCoverPhoto().getCreatedAt())
                                .build()
                                : null
                )
                .attachments(
                        attachmentRepository
                                .findByTripActivityIdAndDeletedAtIsNull(activity.getId())
                                .stream()
                                .filter(attachment ->
                                        trip.getCoverPhoto() == null ||
                                                !attachment.getId().equals(trip.getCoverPhoto().getId())
                                )
                                .map(attachment -> AttachmentResponse.builder()
                                        .id(attachment.getId())
                                        .originalFileName(attachment.getOriginalFileName())
                                        .fileType(attachment.getFileType())
                                        .fileSize(attachment.getFileSize())
                                        .filePath(attachment.getFilePath())
                                        .uploadedBy(attachment.getUploadedBy().getId())
                                        .createdAt(attachment.getCreatedAt())
                                        .build())
                                .toList()
                ).build();
    }

    @Override
    public TripSummaryResponse toSummaryResponse(Trip trip) {
        if (trip == null) {
            return null;
        }

        Activity activity = trip.getActivity();

        if (activity == null) {
            return null;
        }

        return TripSummaryResponse.builder()
                .activityId(activity.getId())
                .activityName(activity.getActivityName())
                .destination(trip.getDestination())
                .locationName(activity.getLocationName())
                .startActivity(activity.getStartActivity())
                .deadline(activity.getDeadline())
                .status(activity.getStatus())
                .build();
    }
}
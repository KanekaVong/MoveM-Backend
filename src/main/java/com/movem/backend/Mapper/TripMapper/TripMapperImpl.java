package com.movem.backend.Mapper.TripMapper;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Dto.response.TripResponses.TripResponse;
import com.movem.backend.Dto.response.TripResponses.TripSummaryResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Attachment.Attachment;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Mapper.BaseMapper.AbstractBaseMapper;
import com.movem.backend.Repository.AttachmentRepository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripMapperImpl
        extends AbstractBaseMapper<Trip, TripResponse>
        implements TripMapper {

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
                .flightNumber(trip.getFlightNumber())
                .hotelName(trip.getHotelName())
                .stops(
                        tripStopMapper.toResponseList(
                                trip.getStops()
                        )
                )
                .attachments(
                        attachmentRepository
                                .findByTripActivityIdAndDeletedAtIsNull(
                                        activity.getId()
                                )
                                .stream()
                                .map(attachment -> AttachmentResponse.builder()
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
                                        .build()
                                )
                                .toList()
                )
                .build();
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
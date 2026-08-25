package com.movem.backend.Mapper.TripMapper;

import com.movem.backend.Dto.response.TripResponses.TripStopResponse;
import com.movem.backend.Entity.Trip.TripStop;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TripStopMapperImpl implements TripStopMapper {

    @Override
    public TripStopResponse toResponse(TripStop stop) {
        if (stop == null) {
            return null;
        }
        return TripStopResponse.builder()
                .id(stop.getId())
                .locationName(stop.getLocationName())
                .sequenceOrder(stop.getSequenceOrder())
                .arrivalTime(stop.getArrivalTime())
                .departureTime(stop.getDepartureTime())
                .locationAddress(stop.getLocationAddress())
                .lat(stop.getLat())
                .lng(stop.getLng())
                .googlePlaceId(stop.getGooglePlaceId())
                .isCompleted(stop.getIsCompleted())
                .build();
    }

    @Override
    public List<TripStopResponse> toResponseList(List<TripStop> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}

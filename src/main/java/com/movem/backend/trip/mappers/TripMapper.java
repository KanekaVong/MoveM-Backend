package com.movem.backend.trip.mappers;

import com.movem.backend.trip.dtos.responses.TripResponse;
import com.movem.backend.trip.dtos.responses.TripSummaryResponse;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.BaseMapper.BaseMapper;

public interface TripMapper
        extends BaseMapper<Trip, TripResponse> {

    TripSummaryResponse toSummaryResponse(Trip trip);
}
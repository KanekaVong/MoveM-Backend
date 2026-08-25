package com.movem.backend.Mapper.TripMapper;

import com.movem.backend.Dto.response.TripResponses.TripResponse;
import com.movem.backend.Dto.response.TripResponses.TripSummaryResponse;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Mapper.BaseMapper.BaseMapper;

public interface TripMapper
        extends BaseMapper<Trip, TripResponse> {

    TripSummaryResponse toSummaryResponse(Trip trip);
}
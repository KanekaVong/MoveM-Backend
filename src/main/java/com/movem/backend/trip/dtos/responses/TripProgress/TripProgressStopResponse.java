package com.movem.backend.trip.dtos.responses.TripProgress;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripProgressStopResponse {
     Integer id;
     Integer sequenceOrder;
     String locationName;
     String locationAddress;
     BigDecimal lat;
     BigDecimal lng;
     Boolean isCompleted;
}
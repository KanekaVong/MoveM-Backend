package com.movem.backend.fitness.workout.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessWorkoutRoutePointResponse {
     Long id;
     Integer pointSequence;
     BigDecimal latitude;
     BigDecimal longitude;
     BigDecimal accuracy;
     BigDecimal altitude;
     LocalDateTime recordedAt;
}
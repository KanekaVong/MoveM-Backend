package com.movem.backend.Dto.response.FitnessResponse.Workout;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class FitnessWorkoutRoutePointResponse {

    private Long id;
    private Integer pointSequence;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal accuracy;
    private BigDecimal altitude;
    private LocalDateTime recordedAt;
}
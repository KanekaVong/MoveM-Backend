package com.movem.backend.Dto.request.FitnessRequest.Workout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class WorkoutRoutePointsRequest {

    @NotEmpty(message = "At least one route point is required.")
    @Valid
    private List<RoutePointRequest> points;

    @Getter
    @Setter
    public static class RoutePointRequest {

        private Integer pointSequence;

        private BigDecimal latitude;

        private BigDecimal longitude;

        private BigDecimal accuracy;

        private BigDecimal altitude;

        private LocalDateTime recordedAt;
    }
}
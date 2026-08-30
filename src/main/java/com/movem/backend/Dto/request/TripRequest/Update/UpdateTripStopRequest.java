package com.movem.backend.Dto.request.TripRequest.Update;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTripStopRequest {

    @NotBlank
    private String locationName;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    private String locationAddress;

    private BigDecimal lat;

    private BigDecimal lng;

    private String googlePlaceId;

    private String coordinates;

    private Boolean isCompleted;
}

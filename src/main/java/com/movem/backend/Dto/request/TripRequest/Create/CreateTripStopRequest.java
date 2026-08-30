package com.movem.backend.Dto.request.TripRequest.Create;

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
public class CreateTripStopRequest {

    @NotBlank
    private String locationName;

    // Optional — appended to the end of the itinerary (by sequence_order) if omitted
    private Integer sequenceOrder;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    private String locationAddress;

    // Left nullable on purpose: lets someone jot down "grab lunch somewhere" before
    // pinning an exact place from Search Destinations.
    private BigDecimal lat;

    private BigDecimal lng;

    private String googlePlaceId;

    private String coordinates;
}

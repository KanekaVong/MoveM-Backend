package com.movem.backend.trip.dtos.requests.Create;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTripStopRequest {
    @NotBlank
    String locationName;
    Integer sequenceOrder;
    LocalDateTime arrivalTime;
    LocalDateTime departureTime;
    String locationAddress;
    BigDecimal lat;
    BigDecimal lng;
    String googlePlaceId;
    String coordinates;
}

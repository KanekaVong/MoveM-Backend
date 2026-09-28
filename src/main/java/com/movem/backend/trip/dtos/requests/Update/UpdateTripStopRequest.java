package com.movem.backend.trip.dtos.requests.Update;

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
public class UpdateTripStopRequest {
    Integer id;
    @NotBlank
    String locationName;
    LocalDateTime arrivalTime;
    LocalDateTime departureTime;
    String locationAddress;
    BigDecimal lat;
    BigDecimal lng;
    String googlePlaceId;
    String coordinates;
    Boolean isCompleted;
}

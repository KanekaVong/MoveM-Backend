package com.movem.backend.trip.dtos.requests.Create;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTripBookmarkRequest {
    @NotBlank
    String locationName;
    String locationAddress;
    BigDecimal lat;
    BigDecimal lng;
    String googlePlaceId;
}

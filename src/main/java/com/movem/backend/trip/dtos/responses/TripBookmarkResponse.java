package com.movem.backend.trip.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripBookmarkResponse {
     Integer id;
     String locationName;
     String locationAddress;
     BigDecimal lat;
     BigDecimal lng;
     String googlePlaceId;
     LocalDateTime createdAt;
}

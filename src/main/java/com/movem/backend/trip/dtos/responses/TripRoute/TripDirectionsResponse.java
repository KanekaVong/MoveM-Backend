package com.movem.backend.trip.dtos.responses.TripRoute;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripDirectionsResponse {
     String mapsUrl;
     BigDecimal destinationLat;
     BigDecimal destinationLng;
     String googlePlaceId;
}

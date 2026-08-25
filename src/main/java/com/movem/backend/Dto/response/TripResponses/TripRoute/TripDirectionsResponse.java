package com.movem.backend.Dto.response.TripResponses.TripRoute;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDirectionsResponse {

    // Ready-to-open deep link — works with the Google Maps app or the web, no API key needed
    private String mapsUrl;

    private BigDecimal destinationLat;

    private BigDecimal destinationLng;

    private String googlePlaceId;
}

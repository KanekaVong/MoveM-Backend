package com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class NearbyPlaceResponse {

    private String name;

    private String type;

    private String address;

    private BigDecimal lat;

    private BigDecimal lng;

    private BigDecimal distanceKm;
}
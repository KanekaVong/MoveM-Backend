package com.movem.backend.Service.TripServices.NearByPlaces;

import com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces.NearbyPlaceResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class NearbyPlacesService {

    public List<NearbyPlaceResponse> findNearbyPlaces(
            BigDecimal lat,
            BigDecimal lng,
            String type,
            double radiusKm
    ) {

        // Temporary implementation.
        // T8.9 will replace this with an external Places API.

        return List.of();
    }
}
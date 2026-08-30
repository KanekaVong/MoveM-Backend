package com.movem.backend.Service.TripServices.TripRouteServices;

import org.springframework.stereotype.Service;

@Service
public class TripTravelTimeService {

    private static final double WALKING_SPEED_KMH = 5.0;
    private static final double DRIVING_SPEED_KMH = 40.0;
    private static final double CYCLING_SPEED_KMH = 15.0;
    private static final double RIDING_SPEED_KMH = 35.0;

    public double calculateTravelTimeMinutes(
            double distanceKm,
            String travelMode
    ) {

        double speedKmh;

        switch (travelMode.toUpperCase()) {

            case "WALKING":
                speedKmh = WALKING_SPEED_KMH;
                break;

            case "CYCLING":
                speedKmh = CYCLING_SPEED_KMH;
                break;

            case "DRIVING":
                speedKmh = DRIVING_SPEED_KMH;
                break;

            case "RIDING":
                speedKmh = RIDING_SPEED_KMH;
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported travel mode: " + travelMode
                );
        }

        double hours = distanceKm / speedKmh;

        return hours * 60;
    }
}
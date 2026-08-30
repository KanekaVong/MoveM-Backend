package com.movem.backend.Service.TripServices.TripRouteServices;

import com.movem.backend.Entity.Trip.TripStop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripDistanceService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public double calculateDistanceKm(
            TripStop from,
            TripStop to
    ) {

        if (from.getLat() == null ||
                from.getLng() == null ||
                to.getLat() == null ||
                to.getLng() == null) {

            throw new IllegalArgumentException(
                    "Both stops must have latitude and longitude."
            );
        }

        double lat1 = from.getLat().doubleValue();
        double lon1 = from.getLng().doubleValue();

        double lat2 = to.getLat().doubleValue();
        double lon2 = to.getLng().doubleValue();

        double latDistance =
                Math.toRadians(lat2 - lat1);

        double lonDistance =
                Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(latDistance / 2)
                        * Math.sin(latDistance / 2)
                        +
                        Math.cos(Math.toRadians(lat1))
                                * Math.cos(Math.toRadians(lat2))
                                * Math.sin(lonDistance / 2)
                                * Math.sin(lonDistance / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return EARTH_RADIUS_KM * c;
    }

    public double calculateTotalDistanceKm(
            List<TripStop> stops
    ) {

        if (stops == null || stops.size() < 2) {
            return 0.0;
        }

        double total = 0.0;

        for (int i = 0; i < stops.size() - 1; i++) {

            TripStop current = stops.get(i);
            TripStop next = stops.get(i + 1);

            total += calculateDistanceKm(
                    current,
                    next
            );
        }

        return total;
    }
}
package com.movem.backend.Service.TripServices.TripRouteServices;

import com.movem.backend.Entity.Trip.TripStop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripRouteOptimizationService {

    private final TripDistanceService tripDistanceService;

    public List<TripStop> optimizeRoute(
            List<TripStop> stops
    ) {

        if (stops == null || stops.size() <= 2) {
            return new ArrayList<>(stops);
        }

        List<TripStop> remaining =
                new ArrayList<>(stops);

        List<TripStop> optimized =
                new ArrayList<>();

        // Keep the original first stop as the starting point
        TripStop current =
                remaining.remove(0);

        optimized.add(current);

        while (!remaining.isEmpty()) {

            TripStop nearestStop = null;
            double shortestDistance =
                    Double.MAX_VALUE;

            for (TripStop candidate : remaining) {

                double distance =
                        tripDistanceService
                                .calculateDistanceKm(
                                        current,
                                        candidate
                                );

                if (distance < shortestDistance) {

                    shortestDistance = distance;
                    nearestStop = candidate;
                }
            }

            optimized.add(nearestStop);
            remaining.remove(nearestStop);

            current = nearestStop;
        }

        return optimized;
    }
}
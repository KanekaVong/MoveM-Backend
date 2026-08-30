package com.movem.backend.Service.Implement.FitnessServices.Workout;

import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutRoutePoint;
import com.movem.backend.Service.FitnessServices.Workout.WorkoutRouteCalculationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class WorkoutRouteCalculationServiceImpl
        implements WorkoutRouteCalculationService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public BigDecimal calculateDistance(
            List<FitnessWorkoutRoutePoint> points
    ) {

        if (points == null || points.size() < 2) {
            return BigDecimal.ZERO;
        }

        double totalDistance = 0.0;

        for (int i = 1; i < points.size(); i++) {

            FitnessWorkoutRoutePoint previous =
                    points.get(i - 1);

            FitnessWorkoutRoutePoint current =
                    points.get(i);

            double latitude1 =
                    Math.toRadians(
                            previous.getLatitude().doubleValue()
                    );

            double longitude1 =
                    Math.toRadians(
                            previous.getLongitude().doubleValue()
                    );

            double latitude2 =
                    Math.toRadians(
                            current.getLatitude().doubleValue()
                    );

            double longitude2 =
                    Math.toRadians(
                            current.getLongitude().doubleValue()
                    );

            double deltaLatitude =
                    latitude2 - latitude1;

            double deltaLongitude =
                    longitude2 - longitude1;

            double a =
                    Math.sin(deltaLatitude / 2)
                            * Math.sin(deltaLatitude / 2)
                            +
                            Math.cos(latitude1)
                                    * Math.cos(latitude2)
                                    * Math.sin(deltaLongitude / 2)
                                    * Math.sin(deltaLongitude / 2);

            double c =
                    2 * Math.atan2(
                            Math.sqrt(a),
                            Math.sqrt(1 - a)
                    );

            totalDistance +=
                    EARTH_RADIUS_KM * c;
        }

        return BigDecimal.valueOf(totalDistance)
                .setScale(
                        3,
                        RoundingMode.HALF_UP
                );
    }

    @Override
    public BigDecimal calculateSpeed(
            BigDecimal distanceKm,
            Integer durationSeconds
    ) {

        if (
                distanceKm == null ||
                        durationSeconds == null ||
                        distanceKm.compareTo(BigDecimal.ZERO) <= 0 ||
                        durationSeconds <= 0
        ) {
            return BigDecimal.ZERO;
        }

        return distanceKm
                .divide(
                        BigDecimal.valueOf(durationSeconds),
                        6,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(3600))
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }

    @Override
    public BigDecimal calculatePace(
            BigDecimal distanceKm,
            Integer durationSeconds
    ) {

        if (distanceKm == null
                || durationSeconds == null
                || distanceKm.compareTo(BigDecimal.ZERO) <= 0
                || durationSeconds <= 0) {

            return BigDecimal.ZERO;
        }

        BigDecimal secondsPerKm =
                BigDecimal.valueOf(durationSeconds)
                        .divide(
                                distanceKm,
                                6,
                                RoundingMode.HALF_UP
                        );

        return secondsPerKm
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }


}
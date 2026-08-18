package com.movem.backend.Util.FitnessUtil;

import com.movem.backend.model.enums.Fitness.FitnessGoalMetric;

import java.math.BigDecimal;

public final class FitnessMetricGoalPreset {

    private FitnessMetricGoalPreset() {
    }

    public static BigDecimal getTarget(
            FitnessGoalMetric metric
    ) {

        return switch (metric) {

            case DAILY_STEPS ->
                    BigDecimal.valueOf(10_000);

            case DAILY_DISTANCE ->
                    BigDecimal.valueOf(5);

            case WEEKLY_STEPS ->
                    BigDecimal.valueOf(30_000);

            case WEEKLY_DISTANCE ->
                    BigDecimal.valueOf(20);

            case WEEKLY_WORKOUT_COUNT ->
                    BigDecimal.valueOf(3);
        };
    }

    public static String getUnit(
            FitnessGoalMetric metric
    ) {

        return switch (metric) {

            case DAILY_STEPS,
                 WEEKLY_STEPS ->
                    "STEPS";

            case DAILY_DISTANCE,
                 WEEKLY_DISTANCE ->
                    "KM";

            case WEEKLY_WORKOUT_COUNT ->
                    "WORKOUTS";
        };
    }

    public static String getPeriod(
            FitnessGoalMetric metric
    ) {

        return switch (metric) {

            case DAILY_STEPS,
                 DAILY_DISTANCE ->
                    "DAILY";

            case WEEKLY_STEPS,
                 WEEKLY_DISTANCE,
                 WEEKLY_WORKOUT_COUNT ->
                    "WEEKLY";
        };
    }
}
package com.movem.backend.Service.Implement.FitnessServices.ProfileAndGoal;

import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessMetricProgressResponse;
import com.movem.backend.Dto.response.StatisticsResponse.FitnessStatisticsResponse;
import com.movem.backend.Service.FitnessServices.ProfileAndGoal.FitnessMetricProgressService;
import com.movem.backend.Util.FitnessUtil.FitnessMetricGoalPreset;
import com.movem.backend.model.enums.Fitness.FitnessGoalMetric;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FitnessMetricProgressServiceImpl
        implements FitnessMetricProgressService {

    @Override
    public List<FitnessMetricProgressResponse> getMetricProgress(
            FitnessStatisticsResponse statistics
    ) {

        return Arrays.stream(
                        FitnessGoalMetric.values()
                )
                .map(
                        metric ->
                                buildProgress(
                                        metric,
                                        statistics
                                )
                )
                .toList();


    }



    private FitnessMetricProgressResponse buildProgress(
            FitnessGoalMetric metric,
            FitnessStatisticsResponse statistics
    ) {

        BigDecimal current =
                getCurrentValue(
                        metric,
                        statistics
                );

        BigDecimal target =
                FitnessMetricGoalPreset.getTarget(
                        metric
                );

        BigDecimal remaining =
                target
                        .subtract(current)
                        .max(BigDecimal.ZERO);

        BigDecimal progressPercent =
                BigDecimal.ZERO;

        if (
                target.compareTo(BigDecimal.ZERO) > 0
        ) {

            progressPercent =
                    current
                            .divide(
                                    target,
                                    6,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(
                                    BigDecimal.valueOf(100)
                            )
                            .min(
                                    BigDecimal.valueOf(100)
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );
        }

        boolean completed =
                current.compareTo(target) >= 0;

        return FitnessMetricProgressResponse.builder()
                .metricType(metric)
                .current(current)
                .target(target)
                .remaining(remaining)
                .progressPercent(progressPercent)
                .unit(
                        FitnessMetricGoalPreset.getUnit(
                                metric
                        )
                )
                .period(
                        FitnessMetricGoalPreset.getPeriod(
                                metric
                        )
                )
                .completed(completed)
                .build();
    }

    private BigDecimal getCurrentValue(
            FitnessGoalMetric metric,
            FitnessStatisticsResponse statistics
    ) {

        return switch (metric) {

            case DAILY_STEPS ->
                    BigDecimal.valueOf(
                            statistics.getStepsToday()
                    );

            case DAILY_DISTANCE ->
                    statistics.getDistanceToday();

            case WEEKLY_STEPS ->
                    BigDecimal.valueOf(
                            statistics.getStepsThisWeek()
                    );

            case WEEKLY_DISTANCE ->
                    statistics.getDistanceThisWeek();

            case WEEKLY_WORKOUT_COUNT ->
                    BigDecimal.valueOf(
                            statistics.getWorkoutsThisWeek()
                    );
        };
    }
}
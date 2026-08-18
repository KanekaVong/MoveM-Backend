package com.movem.backend.Service.Implement.StatisticsServices;

import com.movem.backend.Dto.response.StatisticsResponse.FitnessStatisticsResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutSessionRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.ProfileAndGoal.FitnessMetricProgressService;
import com.movem.backend.Service.StatisticsServices.FitnessStatisticsService;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FitnessStatisticsServiceImpl
        implements FitnessStatisticsService {

    private final FitnessMetricProgressService fitnessMetricProgressService;
    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final CurrentUserService currentUserService;

    @Override
    public FitnessStatisticsResponse getMyFitnessStatistics() {

        User currentUser =
                currentUserService.getCurrentUser();

        LocalDateTime now =
                LocalDateTime.now();

        LocalDateTime startToday =
                now.toLocalDate().atStartOfDay();

        LocalDateTime endToday =
                startToday.plusDays(1);

        LocalDate currentDate =
                now.toLocalDate();

        LocalDate startWeekDate =
                currentDate.with(
                        DayOfWeek.MONDAY
                );

        LocalDateTime startWeek =
                startWeekDate.atStartOfDay();

        LocalDateTime endWeek =
                startWeek.plusDays(7);

        List<FitnessWorkoutSession> allWorkouts =
                workoutSessionRepository
                        .findByUserAndStatusAndActivity_StatusNotOrderByFinishedAtDesc(
                                currentUser,
                                FitnessWorkoutStatus.COMPLETED,
                                ActivityStatus.DELETED
                        );

        List<FitnessWorkoutSession> todayWorkouts =
                workoutSessionRepository
                        .findByUserAndStatusAndActivity_StatusNotAndFinishedAtBetweenOrderByFinishedAtDesc(
                                currentUser,
                                FitnessWorkoutStatus.COMPLETED,
                                ActivityStatus.DELETED,
                                startToday,
                                endToday
                        );

        List<FitnessWorkoutSession> weekWorkouts =
                workoutSessionRepository
                        .findByUserAndStatusAndActivity_StatusNotAndFinishedAtBetweenOrderByFinishedAtDesc(
                                currentUser,
                                FitnessWorkoutStatus.COMPLETED,
                                ActivityStatus.DELETED,
                                startWeek,
                                endWeek
                        );

        long totalWorkouts =
                allWorkouts.size();

        long workoutsToday =
                todayWorkouts.size();

        long workoutsThisWeek =
                weekWorkouts.size();


        long totalSteps =
                calculateSteps(allWorkouts);

        long stepsToday =
                calculateSteps(todayWorkouts);

        long stepsThisWeek =
                calculateSteps(weekWorkouts);


        BigDecimal totalDistance =
                calculateDistance(allWorkouts);

        BigDecimal distanceToday =
                calculateDistance(todayWorkouts);

        BigDecimal distanceThisWeek =
                calculateDistance(weekWorkouts);


        BigDecimal totalCalories =
                calculateCalories(allWorkouts);

        BigDecimal caloriesToday =
                calculateCalories(todayWorkouts);

        BigDecimal caloriesThisWeek =
                calculateCalories(weekWorkouts);

        FitnessStatisticsResponse statistics =
                FitnessStatisticsResponse.builder()
                .totalWorkouts(totalWorkouts)
                .workoutsToday(workoutsToday)
                .workoutsThisWeek(workoutsThisWeek)

                .totalSteps(totalSteps)
                .stepsToday(stepsToday)
                .stepsThisWeek(stepsThisWeek)

                .totalDistance(totalDistance)
                .distanceToday(distanceToday)
                .distanceThisWeek(distanceThisWeek)

                .caloriesToday(caloriesToday)
                .caloriesThisWeek(caloriesThisWeek)
                .totalCalories(totalCalories)
                .build();

        return statistics.toBuilder()
                .metricGoals(
                        fitnessMetricProgressService
                                .getMetricProgress(statistics)
                )
                .build();
    }
    private long calculateSteps(
            List<FitnessWorkoutSession> workouts
    ) {

        return workouts.stream()
                .mapToLong(
                        workout ->
                                workout.getSteps() != null
                                        ? workout.getSteps()
                                        : 0
                )
                .sum();
    }


    private BigDecimal calculateDistance(
            List<FitnessWorkoutSession> workouts
    ) {

        return workouts.stream()
                .map(
                        workout ->
                                workout.getDistance() != null
                                        ? workout.getDistance()
                                        : BigDecimal.ZERO
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }


    private BigDecimal calculateCalories(
            List<FitnessWorkoutSession> workouts
    ) {

        return workouts.stream()
                .map(
                        workout ->
                                workout.getCaloriesBurned() != null
                                        ? workout.getCaloriesBurned()
                                        : BigDecimal.ZERO
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }


    private long calculateDuration(
            List<FitnessWorkoutSession> workouts
    ) {

        return workouts.stream()
                .mapToLong(
                        workout ->
                                workout.getDurationSeconds() != null
                                        ? workout.getDurationSeconds()
                                        : 0
                )
                .sum();
    }
}
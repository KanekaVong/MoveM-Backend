package com.movem.backend.Service.Implement.FitnessServices.Workout;

import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Entity.Fitness.ProfileAndGoal.FitnessProfile;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Repository.FitnessRepository.ProfileAndGoal.FitnessProfileRepository;
import com.movem.backend.Service.FitnessServices.Workout.CalorieCalculationService;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CalorieCalculationServiceImpl
        implements CalorieCalculationService {

    private final FitnessProfileRepository fitnessProfileRepository;


    @Override
    public BigDecimal calculateCalories(
            User user,
            FitnessWorkoutSession session
    ) {

        FitnessProfile profile =
                fitnessProfileRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Fitness profile not found."
                                )
                        );


        if (profile.getWeight() == null) {
            throw new IllegalArgumentException(
                    "Weight is required to calculate calories."
            );
        }


        BigDecimal weightKg =
                profile.getWeight();


        if (
                session.getDurationSeconds() == null ||
                        session.getDurationSeconds() <= 0
        ) {
            return BigDecimal.ZERO;
        }

        BigDecimal durationHours =
                BigDecimal.valueOf(
                                session.getDurationSeconds()
                        )
                        .divide(
                                BigDecimal.valueOf(3600),
                                6,
                                RoundingMode.HALF_UP
                        );


        BigDecimal met =
                getMetValue(
                        session.getWorkoutType()
                );


        return met
                .multiply(weightKg)
                .multiply(durationHours)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private String formatPace(BigDecimal secondsPerKm) {

        if (secondsPerKm == null ||
                secondsPerKm.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        long totalSeconds =
                secondsPerKm
                        .setScale(0, RoundingMode.HALF_UP)
                        .longValue();

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format(
                "%d:%02d",
                minutes,
                seconds
        );
    }

    private BigDecimal getMetValue(
            WorkoutType workoutType
    ) {

        if (workoutType == null) {
            return BigDecimal.ZERO;
        }


        return switch (workoutType) {

            case RUNNING ->
                    BigDecimal.valueOf(9.8);

            case WALKING ->
                    BigDecimal.valueOf(3.5);

            case CYCLING ->
                    BigDecimal.valueOf(7.5);

            case SWIMMING ->
                    BigDecimal.valueOf(8.0);

            default ->
                    BigDecimal.valueOf(5.0);
        };
    }
}
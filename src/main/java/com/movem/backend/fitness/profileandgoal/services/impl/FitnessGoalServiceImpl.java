package com.movem.backend.fitness.profileandgoal.services.impl;

import com.movem.backend.fitness.profileandgoal.dtos.requests.CreateFitnessGoalRequest;
import com.movem.backend.fitness.profileandgoal.dtos.responses.FitnessGoalResponse;
import com.movem.backend.fitness.profileandgoal.entities.FitnessGoal;
import com.movem.backend.fitness.profileandgoal.entities.FitnessProfile;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.fitness.profileandgoal.mappers.FitnessGoalMapper;
import com.movem.backend.fitness.profileandgoal.repositories.FitnessGoalRepository;
import com.movem.backend.fitness.profileandgoal.repositories.FitnessProfileRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.fitness.profileandgoal.services.FitnessGoalService;
import com.movem.backend.shared.analysis.statistics.services.FitnessStatisticsService;
import com.movem.backend.commons.enums.Fitness.GoalType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessGoalServiceImpl
        implements FitnessGoalService {

    private final FitnessGoalRepository fitnessGoalRepository;
    private final FitnessProfileRepository fitnessProfileRepository;
    private final CurrentUserService currentUserService;
    private final FitnessStatisticsService fitnessStatisticsService;
    private final FitnessGoalMapper fitnessGoalMapper;


    @Override
    public FitnessGoalResponse createGoal(
            CreateFitnessGoalRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessProfile profile =
                fitnessProfileRepository
                        .findByUser(currentUser)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness profile not found."
                                ));

        if (profile.getWeight() == null) {
            throw new IllegalArgumentException(
                    "Current weight is required before creating a fitness goal."
            );
        }

        LocalDate today = LocalDate.now();

        if (!request.getTargetTimeline().isAfter(today)) {
            throw new IllegalArgumentException(
                    "Target timeline must be in the future."
            );
        }

        long days =
                ChronoUnit.DAYS.between(
                        today,
                        request.getTargetTimeline()
                );

        if (days < 14) {
            throw new IllegalArgumentException(
                    "Fitness goals must have a timeline of at least 2 weeks."
            );
        }

        BigDecimal currentWeight =
                profile.getWeight();

        BigDecimal targetWeight =
                request.getTargetWeight();

        BigDecimal weightDifference =
                currentWeight.subtract(targetWeight);

        BigDecimal estimatedDailyDeficit =
                calculateDailyDeficit(
                        request.getGoalType(),
                        weightDifference,
                        days
                );

        Integer durationWeeks =
                (int) Math.ceil(days / 7.0);

        FitnessGoal goal =
                new FitnessGoal();

        goal.setUser(currentUser);

        goal.setGoalType(
                request.getGoalType()
        );

        goal.setTargetWeight(
                targetWeight
        );

        goal.setTargetTimeline(
                request.getTargetTimeline()
        );

        goal.setWorkoutLevel(
                request.getWorkoutLevel()
        );

        goal.setEstimatedWeightChange(
                weightDifference.abs()
        );

        goal.setEstimatedDailyDeficit(
                estimatedDailyDeficit
        );

        goal.setStatus(
                "ACTIVE"
        );

        goal.setCreatedAt(
                LocalDateTime.now()
        );

        goal.setUpdatedAt(
                LocalDateTime.now()
        );

        FitnessGoal saved =
                fitnessGoalRepository.save(goal);

        return fitnessGoalMapper.toResponse(saved);
    }

    private BigDecimal calculateDailyDeficit(
            GoalType goalType,
            BigDecimal weightDifference,
            long days
    ) {

        if (goalType != GoalType.WEIGHT_LOSS) {
            return BigDecimal.ZERO;
        }

        if (weightDifference.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Target weight must be lower than current weight for weight loss."
            );
        }

        BigDecimal caloriesPerKg =
                BigDecimal.valueOf(7700);

        BigDecimal totalDeficit =
                weightDifference.multiply(
                        caloriesPerKg
                );

        return totalDeficit.divide(
                BigDecimal.valueOf(days),
                2,
                RoundingMode.HALF_UP
        );
    }

    @Override
    @Transactional(readOnly = true)
    public FitnessGoalResponse getGoal(
            Integer goalId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessGoal goal =
                fitnessGoalRepository
                        .findByIdAndUser(
                                goalId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness goal not found."
                                ));

        return fitnessGoalMapper.toResponse(goal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FitnessGoalResponse> getMyGoals() {

        User currentUser =
                currentUserService.getCurrentUser();

        return fitnessGoalRepository
                .findByUserOrderByCreatedAtDesc(
                        currentUser
                )
                .stream()
                .map(fitnessGoalMapper::toResponse)
                .toList();
    }

    @Override
    public FitnessGoalResponse updateGoal(
            Integer goalId,
            CreateFitnessGoalRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessGoal goal =
                fitnessGoalRepository
                        .findByIdAndUser(
                                goalId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness goal not found."
                                ));

        FitnessProfile profile =
                fitnessProfileRepository
                        .findByUser(currentUser)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness profile not found."
                                ));

        if (profile.getWeight() == null) {
            throw new IllegalArgumentException(
                    "Current weight is required."
            );
        }

        LocalDate today = LocalDate.now();

        if (!request.getTargetTimeline().isAfter(today)) {
            throw new IllegalArgumentException(
                    "Target timeline must be in the future."
            );
        }

        long days =
                ChronoUnit.DAYS.between(
                        today,
                        request.getTargetTimeline()
                );

        if (days < 14) {
            throw new IllegalArgumentException(
                    "Fitness goals must have a timeline of at least 2 weeks."
            );
        }

        BigDecimal currentWeight =
                profile.getWeight();

        BigDecimal targetWeight =
                request.getTargetWeight();

        BigDecimal weightDifference =
                currentWeight.subtract(
                        targetWeight
                );

        BigDecimal dailyDeficit =
                calculateDailyDeficit(
                        request.getGoalType(),
                        weightDifference,
                        days
                );

        goal.setGoalType(
                request.getGoalType()
        );

        goal.setTargetWeight(
                targetWeight
        );

        goal.setTargetTimeline(
                request.getTargetTimeline()
        );

        goal.setWorkoutLevel(
                request.getWorkoutLevel()
        );

        goal.setEstimatedWeightChange(
                weightDifference.abs()
        );

        goal.setEstimatedDailyDeficit(
                dailyDeficit
        );

        goal.setUpdatedAt(
                LocalDateTime.now()
        );

        FitnessGoal saved =
                fitnessGoalRepository.save(goal);

        return fitnessGoalMapper.toResponse(saved);
    }

    @Override
    public void deleteGoal(
            Integer goalId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessGoal goal =
                fitnessGoalRepository
                        .findByIdAndUser(
                                goalId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness goal not found."
                                ));

        fitnessGoalRepository.delete(goal);
    }


}

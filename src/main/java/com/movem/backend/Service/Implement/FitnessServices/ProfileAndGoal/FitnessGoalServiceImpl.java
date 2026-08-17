package com.movem.backend.Service.Implement.FitnessServices.ProfileAndGoal;

import com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal.CreateFitnessGoalRequest;
import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessGoalResponse;
import com.movem.backend.Entity.Fitness.ProfileAndGoal.FitnessGoal;
import com.movem.backend.Entity.Fitness.ProfileAndGoal.FitnessProfile;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.FitnessMapper.ProfileAndGoal.FitnessGoalMapper;
import com.movem.backend.Repository.FitnessRepository.ProfileAndGoal.FitnessGoalRepository;
import com.movem.backend.Repository.FitnessRepository.ProfileAndGoal.FitnessProfileRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.ProfileAndGoal.FitnessGoalService;
import com.movem.backend.model.enums.Fitness.GoalType;
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

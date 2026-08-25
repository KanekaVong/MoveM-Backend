package com.movem.backend.Service.FitnessServices.Achievement;

import com.movem.backend.Dto.response.FitnessResponse.Achievement.AchievementResponse;
import com.movem.backend.Dto.response.FitnessResponse.Achievement.UserAchievementResponse;
import com.movem.backend.Entity.Achievement.Achievement;
import com.movem.backend.Entity.Achievement.UserAchievement;
import com.movem.backend.Entity.Achievement.UserAchievementId;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.Repository.FitnessRepository.Achievement.AchievementRepository;
import com.movem.backend.Repository.FitnessRepository.Achievement.UserAchievementRepository;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutSessionRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.Achievement.AchievementService;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final CurrentUserService currentUserService;

    @Override
    public void evaluate(User user, FeatureEvent event) {

        if (user == null || event == null || event.getFeedEvent() == null) {
            return;
        }

        ActivityFeedEvent eventType = event.getFeedEvent();

        switch (eventType) {
            case WORKOUT_COMPLETED -> evaluateWorkoutAchievements(user);
            case CHALLENGE_COMPLETED -> evaluateChallengeAchievements(user);
            default -> {
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAchievementResponse> getMyAchievements() {

        User currentUser =
                currentUserService.getCurrentUser();

        return userAchievementRepository
                .findByUserOrderByEarnedAtDesc(currentUser)
                .stream()
                .map(userAchievement ->
                        UserAchievementResponse.builder()
                                .achievementId(
                                        userAchievement.getAchievement().getId()
                                )
                                .name(
                                        userAchievement.getAchievement().getName()
                                )
                                .description(
                                        userAchievement.getAchievement().getDescription()
                                )
                                .icon(
                                        userAchievement.getAchievement().getIcon()
                                )
                                .conditionType(
                                        userAchievement.getAchievement().getConditionType()
                                )
                                .conditionValue(
                                        userAchievement.getAchievement().getConditionValue()
                                )
                                .earnedAt(
                                        userAchievement.getEarnedAt()
                                )
                                .build()
                )
                .toList();
    }

    private void evaluateWorkoutAchievements(User user) {

        List<FitnessWorkoutSession> sessions =
                workoutSessionRepository.findByUserAndStatus(
                        user,
                        FitnessWorkoutStatus.COMPLETED
                );

        BigDecimal workoutCount =
                BigDecimal.valueOf(sessions.size());

        BigDecimal distance =
                sessions.stream()
                        .map(FitnessWorkoutSession::getDistance)
                        .filter(value -> value != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal steps =
                sessions.stream()
                        .map(FitnessWorkoutSession::getSteps)
                        .filter(value -> value != null)
                        .map(BigDecimal::valueOf)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        evaluateCondition(
                user,
                "WORKOUT_COUNT",
                workoutCount
        );

        evaluateCondition(
                user,
                "DISTANCE",
                distance
        );

        evaluateCondition(
                user,
                "STEPS",
                steps
        );
    }

    private void evaluateChallengeAchievements(User user) {

        evaluateCondition(
                user,
                "CHALLENGE_COMPLETED",
                BigDecimal.ONE
        );
    }

    private void evaluateCondition(
            User user,
            String conditionType,
            BigDecimal currentValue
    ) {

        List<Achievement> achievements =
                achievementRepository.findAll()
                        .stream()
                        .filter(achievement ->
                                conditionType.equals(
                                        achievement.getConditionType()
                                )
                        )
                        .toList();

        for (Achievement achievement : achievements) {

            if (userAchievementRepository
                    .existsByUserAndAchievement(user, achievement)) {
                continue;
            }

            if (currentValue.compareTo(
                    achievement.getConditionValue()
            ) >= 0) {

                unlock(user, achievement);
            }
        }
    }

    private void unlock(
            User user,
            Achievement achievement
    ) {

        UserAchievement userAchievement =
                new UserAchievement();

        userAchievement.setId(
                new UserAchievementId(
                        user.getId(),
                        achievement.getId()
                )
        );

        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setEarnedAt(LocalDateTime.now());

        userAchievementRepository.save(userAchievement);
    }

    @Override
    @Transactional(readOnly = true)
    public long getMyAchievementCount() {

        User currentUser =
                currentUserService.getCurrentUser();

        return userAchievementRepository.countByUser(
                currentUser
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AchievementResponse> getAllAchievements() {

        User currentUser = currentUserService.getCurrentUser();

        List<Achievement> achievements =
                achievementRepository.findAllByOrderByIdAsc();

        List<UserAchievement> earned =
                userAchievementRepository
                        .findByUserOrderByEarnedAtDesc(currentUser);

        Set<Integer> earnedIds =
                earned.stream()
                        .map(userAchievement ->
                                userAchievement.getAchievement().getId())
                        .collect(Collectors.toSet());

        Map<String, BigDecimal> progress =
                calculateProgress(currentUser);

        return achievements.stream()
                .map(achievement -> {

                    BigDecimal current =
                            progress.getOrDefault(
                                    achievement.getConditionType(),
                                    BigDecimal.ZERO
                            );

                    BigDecimal target =
                            achievement.getConditionValue();

                    BigDecimal percentage =
                            target.compareTo(BigDecimal.ZERO) == 0
                                    ? BigDecimal.ZERO
                                    : current
                                    .divide(
                                            target,
                                            4,
                                            RoundingMode.HALF_UP
                                    )
                                    .multiply(
                                            BigDecimal.valueOf(100)
                                    )
                                    .min(BigDecimal.valueOf(100));

                    return AchievementResponse.builder()
                            .achievementId(achievement.getId())
                            .name(achievement.getName())
                            .description(achievement.getDescription())
                            .icon(achievement.getIcon())
                            .category(achievement.getCategory())
                            .conditionType(achievement.getConditionType())
                            .conditionValue(target)
                            .currentProgress(current)
                            .progressPercentage(percentage)
                            .earned(
                                    earnedIds.contains(
                                            achievement.getId()
                                    )
                            )
                            .build();
                })
                .toList();
    }

    private Map<String, BigDecimal> calculateProgress(
            User user
    ) {

        List<FitnessWorkoutSession> sessions =
                workoutSessionRepository.findByUserAndStatus(
                        user,
                        FitnessWorkoutStatus.COMPLETED
                );

        BigDecimal workoutCount =
                BigDecimal.valueOf(sessions.size());

        BigDecimal distance =
                sessions.stream()
                        .map(FitnessWorkoutSession::getDistance)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal steps =
                sessions.stream()
                        .map(FitnessWorkoutSession::getSteps)
                        .filter(Objects::nonNull)
                        .map(BigDecimal::valueOf)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> result = new HashMap<>();

        result.put("WORKOUT_COUNT", workoutCount);
        result.put("DISTANCE", distance);
        result.put("STEPS", steps);

        return result;
    }
}
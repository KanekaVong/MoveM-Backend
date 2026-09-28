package com.movem.backend.fitness.achievement.services.impl;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.commons.Event.FeatureEvent;
import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.fitness.achievement.dtos.responses.AchievementResponse;
import com.movem.backend.fitness.achievement.dtos.responses.UserAchievementResponse;
import com.movem.backend.fitness.achievement.entities.Achievement;
import com.movem.backend.fitness.achievement.entities.UserAchievement;
import com.movem.backend.fitness.achievement.entities.UserAchievementId;
import com.movem.backend.fitness.achievement.repositories.AchievementRepository;
import com.movem.backend.fitness.achievement.repositories.UserAchievementRepository;
import com.movem.backend.fitness.achievement.services.AchievementService;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutSessionRepository;
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
    public List<UserAchievement> evaluate(User user, FeatureEvent event) {
        if (user == null || event == null || event.getFeedEvent() == null) {
            return Collections.emptyList();
        }

        ActivityFeedEvent eventType = event.getFeedEvent();
        List<UserAchievement> achievements = new ArrayList<>();

        switch (eventType) {
            case WORKOUT_COMPLETED -> achievements.addAll(evaluateWorkoutAchievements(user));
            case CHALLENGE_COMPLETED -> achievements.addAll(evaluateChallengeAchievements(user));
            default -> {
            }
        }

        return achievements;
    }

    @Override
    @Transactional(readOnly = true)
    public long getMyAchievementCount() {
        User currentUser = currentUserService.getCurrentUser();
        return userAchievementRepository.countByUser(currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAchievementResponse> getMyAchievements() {
        User currentUser = currentUserService.getCurrentUser();
        List<UserAchievement> achievements = userAchievementRepository.findByUserOrderByEarnedAtDesc(currentUser);

        return toResponse(achievements);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AchievementResponse> getAllAchievements() {
        User currentUser = currentUserService.getCurrentUser();
        List<Achievement> achievements = achievementRepository.findAllByOrderByIdAsc();
        List<UserAchievement> earned = userAchievementRepository.findByUserOrderByEarnedAtDesc(currentUser);
        Set<Integer> earnedIds = earned.stream().map(userAchievement -> userAchievement.getAchievement().getId()).collect(Collectors.toSet());
        Map<String, BigDecimal> progress = calculateProgress(currentUser);

        return achievements.stream()
                .map(achievement -> {

                    BigDecimal current = progress.getOrDefault(achievement.getConditionType(), BigDecimal.ZERO);
                    BigDecimal target = achievement.getConditionValue();
                    BigDecimal percentage = target.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : current.divide(target, 4, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100))
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
                            .earned(earnedIds.contains(achievement.getId()))
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAchievementResponse> getCurrentAchievement() {
        User currentUser = currentUserService.getCurrentUser();
        List<UserAchievement> currentAchievements = userAchievementRepository.findByUserAndNotifiedFalseOrderByEarnedAtDesc(currentUser);

        return toResponse(currentAchievements);
    }
    private List<UserAchievement> evaluateWorkoutAchievements(User user) {
        List<FitnessWorkoutSession> sessions = workoutSessionRepository.findByUserAndStatus(user, FitnessWorkoutStatus.COMPLETED);

        BigDecimal workoutCount = BigDecimal.valueOf(sessions.size());
        BigDecimal distance = sessions.stream()
                .map(FitnessWorkoutSession::getDistance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal steps = sessions.stream()
                .map(FitnessWorkoutSession::getSteps)
                .filter(Objects::nonNull)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<UserAchievement> result = new ArrayList<>();

        result.addAll(evaluateCondition(user, "WORKOUT_COUNT", workoutCount));
        result.addAll(evaluateCondition(user, "DISTANCE", distance));
        result.addAll(evaluateCondition(user, "STEPS", steps));

        return result;
    }

    private List<UserAchievement> evaluateChallengeAchievements(User user) {
        return evaluateCondition(user, "CHALLENGE_COMPLETED", BigDecimal.ONE);
    }

    private List<UserAchievement> evaluateCondition(User user, String conditionType, BigDecimal currentValue) {

        List<Achievement> achievements = achievementRepository.findAll()
                .stream()
                .filter(achievement -> conditionType.equals(achievement.getConditionType()))
                .toList();
        List<UserAchievement> result = new ArrayList<>();

        for (Achievement achievement : achievements) {
            Optional<UserAchievement> existing = userAchievementRepository.findByUserAndAchievement(user, achievement);

            if (existing.isPresent()) {
                UserAchievement userAchievement = existing.get();
                if (userAchievement.isNotified()) {
                    continue;
                }
                result.add(userAchievement);
                continue;
            }

            if (currentValue.compareTo(achievement.getConditionValue()) >= 0) {

                result.add(unlock(user, achievement));
            }
        }

        return result;
    }
    private UserAchievement unlock(User user, Achievement achievement) {
        UserAchievement userAchievement = new UserAchievement();

        userAchievement.setId(new UserAchievementId(user.getId(), achievement.getId()));
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setEarnedAt(LocalDateTime.now());
        userAchievement.setNotified(false);

        return userAchievementRepository.save(userAchievement);
    }

    private UserAchievementResponse toUserAchievementResponse(UserAchievement userAchievement) {

        Achievement achievement = userAchievement.getAchievement();
        return UserAchievementResponse.builder()
                .achievementId(achievement.getId())
                .name(achievement.getName())
                .description(achievement.getDescription())
                .icon(achievement.getIcon())
                .conditionType(achievement.getConditionType())
                .conditionValue(achievement.getConditionValue())
                .earnedAt(userAchievement.getEarnedAt())
                .build();
    }

    private Map<String, BigDecimal> calculateProgress(User user) {
        List<FitnessWorkoutSession> sessions = workoutSessionRepository.findByUserAndStatus(user, FitnessWorkoutStatus.COMPLETED);

        BigDecimal workoutCount = BigDecimal.valueOf(sessions.size());
        BigDecimal distance = sessions.stream()
                        .map(FitnessWorkoutSession::getDistance)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal steps = sessions.stream()
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

    @Override
    @Transactional(readOnly = true)
    public List<UserAchievementResponse> toResponse(List<UserAchievement> achievements) {
        return achievements.stream().map(userAchievement -> {Achievement achievement = userAchievement.getAchievement();
                    return UserAchievementResponse.builder()
                            .achievementId(achievement.getId())
                            .name(achievement.getName())
                            .description(achievement.getDescription())
                            .icon(achievement.getIcon())
                            .conditionType(achievement.getConditionType())
                            .conditionValue(achievement.getConditionValue())
                            .earnedAt(userAchievement.getEarnedAt())
                            .notified(userAchievement.isNotified())
                            .build();}).toList();
    }
}
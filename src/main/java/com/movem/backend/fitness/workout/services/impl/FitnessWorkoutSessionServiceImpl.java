package com.movem.backend.fitness.workout.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movem.backend.commons.enums.Fitness.*;
import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.fitness.workout.dtos.responses.SocialWorkoutResponse;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.fitness.challenges.entities.FitnessChallengeParticipant;
import com.movem.backend.fitness.challenges.entities.GroupFitnessChallenge;
import com.movem.backend.fitness.challenges.entities.SoloChallenge;
import com.movem.backend.fitness.profileandgoal.entities.FitnessProfile;
import com.movem.backend.fitness.workout.dtos.requests.FinishWorkoutRequest;
import com.movem.backend.fitness.workout.dtos.requests.FitnessWorkoutSearchRequest;
import com.movem.backend.fitness.workout.dtos.requests.ShareWorkoutRequest;
import com.movem.backend.fitness.workout.dtos.requests.StartWorkoutRequest;
import com.movem.backend.fitness.workout.dtos.responses.*;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutAnalysis;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutRoutePoint;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.social.friend.entities.Friend;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.commons.Exception.UnauthorizedActionException;
import com.movem.backend.fitness.workout.mappers.FitnessWorkoutSessionMapper;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.fitness.profileandgoal.repositories.FitnessProfileRepository;
import com.movem.backend.commons.Specification.FitnessWorkoutSessionSpecification;
import com.movem.backend.social.comment.repository.CommentRepository;
import com.movem.backend.fitness.challenges.repositories.FitnessChallengeParticipantRepository;
import com.movem.backend.fitness.challenges.repositories.SoloChallengeCatalogRepository;
import com.movem.backend.social.like.repository.KudosRepository;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutAnalysisRepository;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutRoutePointRepository;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutSessionRepository;
import com.movem.backend.social.friend.repositories.FriendRepository;
import com.movem.backend.shared.activity.repositories.ActivityRepository;
import com.movem.backend.shared.attachment.services.AttachmentService;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.fitness.workout.services.CalorieCalculationService;
import com.movem.backend.fitness.workout.services.FitnessWorkoutRouteService;
import com.movem.backend.fitness.workout.services.FitnessWorkoutSessionService;
import com.movem.backend.fitness.workout.services.WorkoutRouteCalculationService;
import com.movem.backend.shared.activity.services.ActivityService;
import com.movem.backend.commons.Event.Factory.Fitness.WorkoutEventFactory;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.commons.Util.FitnessUtil.FitnessChallengeCreateSource;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.shared.ActivityType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessWorkoutSessionServiceImpl implements FitnessWorkoutSessionService {

    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final FitnessProfileRepository fitnessProfileRepository;
    private final SoloChallengeCatalogRepository soloChallengeRepository;
    private final FitnessChallengeParticipantRepository participantRepository;
    private final CalorieCalculationService calorieCalculationService;
    private final CurrentUserService currentUserService;
    private final ActivityService activityService;
    private final ObjectMapper objectMapper;
    private final KudosRepository kudosRepository;
    private final AttachmentRepository attachmentRepository;
    private final CommentRepository commentRepository;
    private final FriendRepository friendRepository;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final WorkoutEventFactory workoutEventFactory;
    private final ActivityRepository activityRepository;
    private final FitnessWorkoutSessionMapper workoutSessionMapper;
    private final FitnessWorkoutRoutePointRepository workoutRoutePointRepository;
    private final FitnessWorkoutRouteService fitnessWorkoutRouteService;
    private final AttachmentService attachmentService;
    private final WorkoutRouteCalculationService workoutRouteCalculationService;
    private final FitnessWorkoutAnalysisRepository analysisRepository;

    @Override
    @Transactional
    public FitnessWorkoutSessionResponse startWorkout(StartWorkoutRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessProfile fitnessProfile = fitnessProfileRepository.findByUser(currentUser).orElse(null);

        SoloChallenge soloChallenge = null;
        FitnessChallengeParticipant participant = null;
        GroupFitnessChallenge groupChallenge = null;

        if (request.getSoloChallengeId() != null) {
            soloChallenge = soloChallengeRepository.findById(request.getSoloChallengeId()).orElseThrow(() -> new ResourceNotFoundException("Solo challenge not found."));
        }
        if (request.getParticipantId() != null) {
            participant = participantRepository.findById(request.getParticipantId()).orElseThrow(() -> new ResourceNotFoundException("Challenge participant not found."));

            if (!participant.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalArgumentException("You can only start your own challenge workout.");
            }

            if (participant.getStatus() != FitnessChallengeParticipantStatus.ACTIVE) {
                throw new IllegalArgumentException("Only an active challenge participant can start a workout.");
            }

            groupChallenge = participant.getChallenge();

            if (groupChallenge == null) {
                throw new IllegalStateException("Challenge participant is not linked to a group fitness challenge.");
            }

            if (groupChallenge.getStatus() == FitnessChallengeStatus.COMPLETE) {
                throw new IllegalArgumentException("This group fitness challenge has already ended.");
            }

            if (groupChallenge.getStatus() == FitnessChallengeStatus.CANCELLED) {
                throw new IllegalArgumentException("This group fitness challenge has been cancelled.");
            }
        }

        if (soloChallenge != null && participant != null) {
            throw new IllegalArgumentException("A workout cannot belong to both a solo challenge and a group challenge.");
        }

        WorkoutType workoutType;

        if (soloChallenge != null) {
            workoutType = soloChallenge.getWorkoutType();
        } else if (groupChallenge != null) {
            workoutType = groupChallenge.getWorkoutType();
        } else {
            workoutType = request.getWorkoutType();
        }

        FitnessChallengeCreateSource source = new FitnessChallengeCreateSource();

        source.setActivityName(workoutType.name() + " Workout");
        source.setDescription("Fitness workout session.");
        source.setStartActivity(LocalDateTime.now());
        source.setDeadline(null);
        source.setParentActivityId(null);

        Activity activity = activityService.createActivity(source, currentUser, ActivityType.FITNESS);

        FitnessWorkoutSession session = new FitnessWorkoutSession();

        session.setActivity(activity);
        session.setUser(currentUser);
        session.setSoloChallenge(soloChallenge);
        session.setGroupChallengeParticipant(participant);
        session.setWorkoutType(workoutType);
        session.setTrackingMode(resolveTrackingMode(workoutType));
        session.setStatus(FitnessWorkoutStatus.IN_PROGRESS);
        session.setStartedAt(LocalDateTime.now());
        session.setDurationSeconds(0);
        session.setSteps(0);
        session.setDistance(BigDecimal.ZERO);
        session.setCaloriesBurned(BigDecimal.ZERO);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        FitnessWorkoutSession saved = workoutSessionRepository.save(session);

        return workoutSessionMapper.toStartResponse(saved, fitnessProfile);
    }

    @Override
    @Transactional
    public void pauseWorkout(Integer sessionId) {

        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));
        if (!session.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can only pause your own workout.");
        }

        if (session.getSoloChallenge() == null) {
            throw new IllegalArgumentException("Pause is only available for solo workouts.");
        }

        if (session.getStatus() != FitnessWorkoutStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Only an active workout can be paused.");
        }

        session.setPausedAt(LocalDateTime.now());
        session.setStatus(FitnessWorkoutStatus.PAUSED);
        session.setUpdatedAt(LocalDateTime.now());

        workoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public void resumeWorkout(Integer sessionId) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        if (!session.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can only resume your own workout.");
        }

        if (session.getSoloChallenge() == null) {
            throw new IllegalArgumentException("Resume is only available for solo workouts.");
        }

        if (session.getStatus() != FitnessWorkoutStatus.PAUSED) {
            throw new IllegalArgumentException("Only a paused workout can be resumed.");
        }

        if (session.getPausedAt() == null) {
            throw new IllegalStateException("Paused time was not recorded.");
        }

        LocalDateTime now = LocalDateTime.now();

        long pausedSeconds = java.time.Duration.between(session.getPausedAt(), now).getSeconds();

        int currentPausedSeconds = session.getTotalPausedSeconds() != null ? session.getTotalPausedSeconds() : 0;

        session.setTotalPausedSeconds(currentPausedSeconds + (int) pausedSeconds);
        session.setPausedAt(null);
        session.setStatus(FitnessWorkoutStatus.IN_PROGRESS);
        session.setUpdatedAt(now);

        workoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public FitnessWorkoutSessionResponse finishWorkout(Integer sessionId, FinishWorkoutRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository
                .findByIdAndUserAndActivity_StatusNot(sessionId, currentUser, ActivityStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        if (session.getStatus() != FitnessWorkoutStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Only an active workout session can be finished.");
        }

        LocalDateTime now = LocalDateTime.now();

        long durationSeconds = Duration.between(session.getStartedAt(), now).getSeconds();

        session.setDurationSeconds((int) durationSeconds);
        session.setSteps(request.getSteps());

        boolean gpsWorkout = session.getTrackingMode() == TrackingMode.GPS;

        if (gpsWorkout) {
            List<FitnessWorkoutRoutePoint> routePoints = workoutRoutePointRepository.findByWorkoutSessionOrderByPointSequenceAsc(session);

            if (!routePoints.isEmpty()) {
                BigDecimal finalDistance = workoutRouteCalculationService.calculateDistance(routePoints);
                session.setDistance(finalDistance);

                if (session.getDurationSeconds() > 0) {

                    BigDecimal finalSpeed = workoutRouteCalculationService.calculateSpeed(finalDistance, session.getDurationSeconds());

                    session.setAverageSpeed(finalSpeed);
                    BigDecimal finalPace = workoutRouteCalculationService.calculatePace(finalDistance, session.getDurationSeconds());
                    session.setAveragePace(finalPace);
                }
            }

        } else {

            if (request.getDistance() != null && request.getDistance().compareTo(BigDecimal.ZERO) >= 0) {
                session.setDistance(request.getDistance());
            }

            if (session.getDistance() != null && session.getDurationSeconds() > 0) {
                BigDecimal finalSpeed = workoutRouteCalculationService.calculateSpeed(session.getDistance(), session.getDurationSeconds());
                session.setAverageSpeed(finalSpeed);
                BigDecimal finalPace = workoutRouteCalculationService.calculatePace(session.getDistance(), session.getDurationSeconds());
                session.setAveragePace(finalPace);
            }
        }

        BigDecimal calories = calorieCalculationService.calculateCalories(currentUser, session);

        session.setCaloriesBurned(calories);
        session.setFinishedAt(now);
        session.setStatus(FitnessWorkoutStatus.COMPLETED);

        Activity activity = session.getActivity();

        if (activity != null) {
            activity.setStatus(ActivityStatus.COMPLETE);
            activity.setUpdatedAt(now);
            activityRepository.save(activity);
        }

        FitnessChallengeParticipant participant = session.getGroupChallengeParticipant();

        if (participant != null && participant.getStatus() == FitnessChallengeParticipantStatus.ACTIVE) {

            participant.setStatus(FitnessChallengeParticipantStatus.COMPLETED);

            participant.setCompletedAt(now);

            participantRepository.save(participant);
        }

        session.setUpdatedAt(now);

        FitnessWorkoutSession saved = workoutSessionRepository.save(session);

        featureEventTrackingService.handle(workoutEventFactory.completed(saved, currentUser));
        return workoutSessionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public List<WorkoutHistoryResponse> searchWorkouts(FitnessWorkoutSearchRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        Specification<FitnessWorkoutSession> specification = FitnessWorkoutSessionSpecification.filter(currentUser, request);

        List<FitnessWorkoutSession> workouts = workoutSessionRepository.findAll(specification);

        return workouts.stream()
                .map(workoutSessionMapper::toHistoryResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<WorkoutHistoryResponse> getWorkoutHistory() {

        User currentUser = currentUserService.getCurrentUser();

        return workoutSessionRepository.findByUserAndStatusAndActivity_StatusNotOrderByFinishedAtDesc(currentUser, FitnessWorkoutStatus.COMPLETED, ActivityStatus.DELETED)
                .stream()
                .map(workoutSessionMapper::toHistoryResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteWorkout(Integer sessionId) {

        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository
                        .findByIdAndUserAndActivity_StatusNot(sessionId,currentUser,ActivityStatus.DELETED)
                        .orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        if (session.getStatus() == FitnessWorkoutStatus.IN_PROGRESS || session.getStatus() == FitnessWorkoutStatus.PAUSED) {
            throw new IllegalArgumentException("An active workout cannot be deleted.");
        }

        Activity activity = session.getActivity();

        if (activity == null) {
            throw new IllegalStateException("Workout session is not linked to an activity.");
        }

        LocalDateTime now = LocalDateTime.now();

        activity.setStatus(ActivityStatus.DELETED);

        activity.setDeletedAt(now);
        activity.setUpdatedAt(now);
        activityRepository.save(activity);

        session.setUpdatedAt(now);

        workoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public FitnessWorkoutSessionResponse getSession(Integer sessionId) {
        User currentUser = currentUserService.getCurrentUser();
        FitnessProfile fitnessProfile = fitnessProfileRepository.findByUser(currentUser).orElse(null);
        FitnessWorkoutSession session = workoutSessionRepository.findByIdAndUserAndActivity_StatusNot(sessionId, currentUser, ActivityStatus.DELETED).orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        return workoutSessionMapper.toResponse(session);
    }

    @Override
    @Transactional
    public List<FitnessWorkoutSessionResponse> getMySessions() {
        User currentUser = currentUserService.getCurrentUser();

        return workoutSessionRepository
                .findByUserAndActivity_StatusNot(currentUser, ActivityStatus.DELETED)
                .stream()
                .map(workoutSessionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public WorkoutDetailsResponse getWorkoutDetails(Integer sessionId) {

        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository
                        .findByIdAndUserAndActivity_StatusNot(sessionId, currentUser, ActivityStatus.DELETED)
                        .orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        Integer durationSeconds = session.getDurationSeconds() != null ? session.getDurationSeconds() : 0;

        BigDecimal distance = session.getDistance() != null ? session.getDistance() : BigDecimal.ZERO;
        BigDecimal calories = session.getCaloriesBurned() != null ? session.getCaloriesBurned() : BigDecimal.ZERO;
        BigDecimal averageSpeed = session.getAverageSpeed() != null ? session.getAverageSpeed() : BigDecimal.ZERO;
        BigDecimal averagePace = session.getAveragePace();

        BigDecimal caloriesPerMinute = BigDecimal.ZERO;

        if (durationSeconds > 0) {
            caloriesPerMinute = calories.divide(BigDecimal.valueOf(durationSeconds), 6, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(60))
                            .setScale(2, RoundingMode.HALF_UP);
        }

        WorkoutChallengeDetailsResponse challenge = null;

        if (session.getSoloChallenge() != null) {
            SoloChallenge soloChallenge = session.getSoloChallenge();

            challenge = WorkoutChallengeDetailsResponse.builder()
                            .type("SOLO")
                            .id(soloChallenge.getId())
                            .participantId(null)
                            .name(soloChallenge.getName())
                            .targetValue(soloChallenge.getTargetValue())
                            .targetUnit(soloChallenge.getTargetUnit() != null ? soloChallenge.getTargetUnit().name() : null)
                            .build();
        }

        else if (session.getGroupChallengeParticipant() != null) {

            FitnessChallengeParticipant participant = session.getGroupChallengeParticipant();

            GroupFitnessChallenge groupChallenge = participant.getChallenge();

            if (groupChallenge != null) {

                challenge = WorkoutChallengeDetailsResponse.builder()
                                .type("GROUP")
                                .id(groupChallenge.getId())
                                .participantId(participant.getId())
                                .name(groupChallenge.getName())
                                .targetValue(groupChallenge.getTargetValue())
                                .targetUnit(groupChallenge.getTargetUnit() != null ? groupChallenge.getTargetUnit().name() : null)
                                .build();
            }
        }

        List<FitnessWorkoutSession> completedWorkouts = workoutSessionRepository.findByUserAndStatusAndActivity_StatusNotOrderByFinishedAtDesc(currentUser, FitnessWorkoutStatus.COMPLETED, ActivityStatus.DELETED
                        );
        int totalCompletedWorkouts = completedWorkouts.size();

        BigDecimal totalDistance = completedWorkouts.stream().map(workout -> workout.getDistance() != null ? workout.getDistance() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCalories = completedWorkouts.stream()
                        .map(workout -> workout.getCaloriesBurned() != null ? workout.getCaloriesBurned() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalWorkoutSeconds = completedWorkouts.stream()
                        .mapToLong(workout -> workout.getDurationSeconds() != null ? workout.getDurationSeconds() : 0).sum();

        String formattedPace = formatPace(session.getAveragePace());

        List<AttachmentResponse> attachments = attachmentRepository.findByWorkoutSessionAndDeletedAtIsNull(session).stream().map(attachmentService::toResponse).toList();

        return WorkoutDetailsResponse.builder()
                .sessionId(session.getId())
                .workoutType(session.getWorkoutType() )
                .status(session.getStatus())
                .startedAt(session.getStartedAt())
                .finishedAt(session.getFinishedAt())
                .durationSeconds(durationSeconds)
                .totalPausedSeconds(session.getTotalPausedSeconds())
                .steps(session.getSteps())
                .distance(distance )
                .caloriesBurned(calories)
                .averagePace(formattedPace)
                .averageSpeed(averageSpeed)
                .caloriesPerMinute(caloriesPerMinute)
                .challenge(challenge)
                .totalCompletedWorkouts(totalCompletedWorkouts)
                .totalDistance(totalDistance)
                .totalCaloriesBurned(totalCalories)
                .totalWorkoutSeconds(totalWorkoutSeconds)
                .attachments(attachments)
                .build();
    }

    @Override
    @Transactional
    public List<WorkoutRoutePointResponse> getWorkoutRoute(Integer sessionId) {

        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository
                        .findByIdAndUserAndActivity_StatusNot(sessionId, currentUser, ActivityStatus.DELETED)
                        .orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        return workoutRoutePointRepository
                .findByWorkoutSessionOrderByPointSequenceAsc(session)
                .stream()
                .map(point ->
                        WorkoutRoutePointResponse.builder()
                                .id(point.getId())
                                .pointSequence(point.getPointSequence())
                                .latitude(point.getLatitude())
                                .longitude(point.getLongitude())
                                .accuracy(point.getAccuracy())
                                .altitude(point.getAltitude())
                                .recordedAt(point.getRecordedAt()).build()
                )
                .toList();
    }

    @Override
    @Transactional
    public SocialWorkoutResponse getSocialWorkout(Integer sessionId) {

        FitnessWorkoutSession session = workoutSessionRepository.findById(sessionId).orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        if (session.getStatus() != FitnessWorkoutStatus.COMPLETED) {
            throw new IllegalArgumentException("Only completed workouts can be viewed socially.");
        }

        User currentUser = currentUserService.getCurrentUser();
        User owner = session.getUser();

        boolean isOwner = owner.getId().equals(currentUser.getId());

        boolean isFriend =
                areFriends(owner, currentUser);
        if (!isOwner && !isFriend) {
            throw new UnauthorizedActionException("You are not allowed to view this workout.");
        }

        List<FitnessWorkoutRoutePointResponse> routePoints = fitnessWorkoutRouteService.getRoute(session);

        List<AttachmentResponse> attachments = attachmentRepository
                        .findByWorkoutSessionAndDeletedAtIsNull(session)
                        .stream()
                        .map(attachmentService::toResponse)
                        .toList();

        return SocialWorkoutResponse.builder()
                .sessionId(session.getId())
                .userId(owner.getId())
                .username(owner.getUsername())
                .workoutType(session.getWorkoutType().name())
                .status(session.getStatus().name())
                .startedAt(session.getStartedAt())
                .finishedAt(session.getFinishedAt())
                .durationSeconds(session.getDurationSeconds())
                .distance(session.getDistance())
                .averagePace(session.getAveragePace())
                .averageSpeed(session.getAverageSpeed())
                .caloriesBurned(session.getCaloriesBurned())
                .steps(session.getSteps())
                .attachments(attachments)
                .points(routePoints)
                .build();
    }

    @Override
    @Transactional
    public FitnessWorkoutSummaryResponse getWorkoutSummary(Integer sessionId) {

        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository.findByIdAndUser(sessionId, currentUser).orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));
        FitnessWorkoutAnalysis analysis = analysisRepository.findByWorkoutSession(session).orElse(null);

        List<String> feedback = List.of();

        if (analysis != null && analysis.getFeedback() != null && !analysis.getFeedback().isBlank()) {

            try {
                feedback = objectMapper.readValue(analysis.getFeedback(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Unable to read workout feedback.");
            }
        }

        return FitnessWorkoutSummaryResponse.builder()
                .sessionId(session.getId())
                .userId(session.getUser().getId())
                .workoutType(session.getWorkoutType().name())
                .trackingMode(session.getTrackingMode().name())
                .status(session.getStatus().name())
                .startedAt(session.getStartedAt())
                .finishedAt(session.getFinishedAt())
                .durationSeconds(session.getDurationSeconds())
                .distance(session.getDistance())
                .steps(session.getSteps())
                .caloriesBurned(session.getCaloriesBurned())
                .reps(analysis == null ? 0 : analysis.getReps())
                .validReps(analysis == null ? 0 : analysis.getValidReps())
                .invalidReps(analysis == null ? 0 : analysis.getInvalidReps())
                .formScore(analysis == null ? null : analysis.getFormScore())
                .feedback(feedback)
                .build();
    }

    @Override
    @Transactional
    public void updateWorkoutSharing(Integer sessionId, ShareWorkoutRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository.findByIdAndUser(sessionId, currentUser).orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        if (session.getStatus() != FitnessWorkoutStatus.COMPLETED) {
            throw new IllegalArgumentException("Only completed workouts can be shared.");
        }

        boolean shared = Boolean.TRUE.equals(request.getShared());

        session.setIsShared(shared);

        if (shared) {
            session.setShareDescription(request.getDescription());
        } else {session.setShareDescription(null);}

        session.setUpdatedAt(LocalDateTime.now());

        workoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public List<SharedWorkoutPostResponse> getSocialWorkoutFeed() {
        User currentUser = currentUserService.getCurrentUser();

        List<Friend> friendships = friendRepository.findByUserOneOrUserTwo(currentUser, currentUser);
        List<User> feedUsers = new ArrayList<>();
        feedUsers.add(currentUser);

        for (Friend friendship : friendships) {
            User friend;

            if (friendship.getUserOne().getId().equals(currentUser.getId())) {
                friend = friendship.getUserTwo();

            } else {
                friend = friendship.getUserOne();
            }

            if (friend != null && feedUsers.stream().noneMatch(user -> user.getId().equals(friend.getId()))) {
                feedUsers.add(friend);
            }
        }

        List<FitnessWorkoutSession> sessions = workoutSessionRepository.findByUserInAndStatusAndIsSharedTrueOrderByFinishedAtDesc(feedUsers, FitnessWorkoutStatus.COMPLETED);

        return sessions.stream().map(session -> {

                    boolean myPost = session.getUser().getId().equals(currentUser.getId());
                    boolean myKudos = kudosRepository.existsByWorkoutSessionAndUser(session, currentUser);
                    long kudosCount = kudosRepository.countByWorkoutSession(session);
                    long commentCount = commentRepository.countByActivity(session.getActivity());

                    List<FitnessWorkoutRoutePointResponse> routePoints = fitnessWorkoutRouteService.getRoute(session);

                    return SharedWorkoutPostResponse.builder()
                            .sessionId(session.getId())
                            .userId(session.getUser().getId())
                            .username(session.getUser().getUsername())
                            .profilePicture(null)
                            .workoutType(session.getWorkoutType().name())
                            .trackingMode(session.getTrackingMode().name())
                            .shareDescription(session.getShareDescription())
                            .attachments(
                                    attachmentRepository
                                            .findByWorkoutSessionAndDeletedAtIsNull(session)
                                            .stream()
                                            .map(attachmentService::toResponse)
                                            .toList()
                            )
                            .distance(session.getDistance())
                            .steps(session.getSteps())
                            .durationSeconds(session.getDurationSeconds())
                            .caloriesBurned(session.getCaloriesBurned())
                            .finishedAt(session.getFinishedAt())
                            .kudosCount(kudosCount)
                            .myKudos(myKudos)
                            .commentCount(commentCount)
                            .myPost(myPost)
                            .points(routePoints)
                            .build();}).toList();
    }
    private String formatPace(BigDecimal secondsPerKm) {
        if (secondsPerKm == null || secondsPerKm.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        long totalSeconds = secondsPerKm.setScale(0, RoundingMode.HALF_UP).longValue();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    private boolean areFriends(User a, User b) {
        User first = a.getId() < b.getId() ? a : b;
        User second = a.getId() < b.getId() ? b : a;

        return friendRepository.existsByUserOneAndUserTwo(first, second);
    }
    private TrackingMode resolveTrackingMode(WorkoutType workoutType) {
        return switch (workoutType) {
            case PUSH_UP -> TrackingMode.POSE;
            case RUNNING, WALKING -> TrackingMode.GPS;
            case BADMINTON, TENNIS -> TrackingMode.STEPS;
            default -> TrackingMode.MANUAL;
        };
    }

}
package com.movem.backend.Service.Implement.FitnessServices.Workout;

import com.movem.backend.Dto.request.FitnessRequest.Workout.StartWorkoutRequest;
import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutProgressRequest;
import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutRoutePointsRequest;
import com.movem.backend.Dto.response.FitnessResponse.Workout.*;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Fitness.Challenge.FitnessChallengeParticipant;
import com.movem.backend.Entity.Fitness.Challenge.GroupFitnessChallenge;
import com.movem.backend.Entity.Fitness.Challenge.SoloChallenge;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutRoutePoint;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.FitnessMapper.Workout.FitnessWorkoutSessionMapper;
import com.movem.backend.Repository.FitnessRepository.Challenge.FitnessChallengeParticipantRepository;
import com.movem.backend.Repository.FitnessRepository.Challenge.SoloChallengeCatalogRepository;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutRoutePointRepository;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutSessionRepository;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.Workout.CalorieCalculationService;
import com.movem.backend.Service.FitnessServices.Workout.FitnessWorkoutSessionService;
import com.movem.backend.Service.FitnessServices.Workout.WorkoutRouteCalculationService;
import com.movem.backend.Service.SharedServices.ActivityService;
import com.movem.backend.Util.FitnessUtil.FitnessChallengeCreateSource;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.model.enums.Fitness.FitnessChallengeParticipantStatus;
import com.movem.backend.model.enums.Fitness.FitnessChallengeStatus;
import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessWorkoutSessionServiceImpl
        implements FitnessWorkoutSessionService {

    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final SoloChallengeCatalogRepository soloChallengeRepository;
    private final FitnessChallengeParticipantRepository participantRepository;
    private final CalorieCalculationService calorieCalculationService;
    private final CurrentUserService currentUserService;
    private final ActivityService activityService;
    private final ActivityRepository activityRepository;
    private final FitnessWorkoutSessionMapper workoutSessionMapper;
    private final FitnessWorkoutRoutePointRepository workoutRoutePointRepository;
    private final WorkoutRouteCalculationService workoutRouteCalculationService;

    @Override
    @Transactional
    public FitnessWorkoutSessionResponse startWorkout(
            StartWorkoutRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        SoloChallenge soloChallenge = null;
        FitnessChallengeParticipant participant = null;
        GroupFitnessChallenge groupChallenge = null;

        if (request.getSoloChallengeId() != null) {

            soloChallenge =
                    soloChallengeRepository
                            .findById(
                                    request.getSoloChallengeId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Solo challenge not found."
                                    )
                            );
        }

        if (request.getParticipantId() != null) {

            participant =
                    participantRepository
                            .findById(
                                    request.getParticipantId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Challenge participant not found."
                                    )
                            );

            if (
                    !participant.getUser()
                            .getId()
                            .equals(currentUser.getId())
            ) {

                throw new IllegalArgumentException(
                        "You can only start your own challenge workout."
                );
            }

            if (
                    participant.getStatus()
                            != FitnessChallengeParticipantStatus.ACTIVE
            ) {

                throw new IllegalArgumentException(
                        "Only an active challenge participant can start a workout."
                );
            }


            groupChallenge =
                    participant.getChallenge();


            if (groupChallenge == null) {

                throw new IllegalStateException(
                        "Challenge participant is not linked to a group fitness challenge."
                );
            }


            if (
                    groupChallenge.getStatus()
                            == FitnessChallengeStatus.COMPLETE
            ) {

                throw new IllegalArgumentException(
                        "This group fitness challenge has already ended."
                );
            }

            if (
                    groupChallenge.getStatus()
                            == FitnessChallengeStatus.CANCELLED
            ) {

                throw new IllegalArgumentException(
                        "This group fitness challenge has been cancelled."
                );
            }
        }

        if (
                soloChallenge != null &&
                        participant != null
        ) {

            throw new IllegalArgumentException(
                    "A workout cannot belong to both a solo challenge and a group challenge."
            );
        }

        WorkoutType workoutType;

        if (soloChallenge != null) {

            workoutType =
                    soloChallenge.getWorkoutType();

        } else if (groupChallenge != null) {

            workoutType =
                    groupChallenge.getWorkoutType();

        } else {

            workoutType =
                    request.getWorkoutType();
        }

        FitnessChallengeCreateSource source =
                new FitnessChallengeCreateSource();

        source.setActivityName(
                workoutType.name() + " Workout"
        );

        source.setDescription(
                "Fitness workout session."
        );

        source.setStartActivity(
                LocalDateTime.now()
        );

        source.setDeadline(
                null
        );

        source.setParentActivityId(
                null
        );


        Activity activity =
                activityService.createActivity(
                        source,
                        currentUser,
                        ActivityType.FITNESS
                );

        FitnessWorkoutSession session =
                new FitnessWorkoutSession();

        session.setActivity(
                activity
        );

        session.setUser(
                currentUser
        );

        session.setSoloChallenge(
                soloChallenge
        );

        session.setGroupChallengeParticipant(
                participant
        );

        session.setWorkoutType(
                workoutType
        );

        session.setStatus(
                FitnessWorkoutStatus.IN_PROGRESS
        );

        session.setStartedAt(
                LocalDateTime.now()
        );

        session.setDurationSeconds(0);

        session.setSteps(0);

        session.setDistance(
                BigDecimal.ZERO
        );

        session.setCaloriesBurned(
                BigDecimal.ZERO
        );

        session.setCreatedAt(
                LocalDateTime.now()
        );

        session.setUpdatedAt(
                LocalDateTime.now()
        );


        FitnessWorkoutSession saved =
                workoutSessionRepository.save(
                        session
                );

        return workoutSessionMapper.toResponse(
                saved
        );
    }

    @Override
    @Transactional
    public void pauseWorkout(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (
                !session.getUser()
                        .getId()
                        .equals(currentUser.getId())
        ) {
            throw new IllegalArgumentException(
                    "You can only pause your own workout."
            );
        }

        if (session.getSoloChallenge() == null) {
            throw new IllegalArgumentException(
                    "Pause is only available for solo workouts."
            );
        }

        if (
                session.getStatus()
                        != FitnessWorkoutStatus.IN_PROGRESS
        ) {
            throw new IllegalArgumentException(
                    "Only an active workout can be paused."
            );
        }

        session.setPausedAt(
                LocalDateTime.now()
        );

        session.setStatus(
                FitnessWorkoutStatus.PAUSED
        );

        session.setUpdatedAt(
                LocalDateTime.now()
        );

        workoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public void resumeWorkout(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (
                !session.getUser()
                        .getId()
                        .equals(currentUser.getId())
        ) {
            throw new IllegalArgumentException(
                    "You can only resume your own workout."
            );
        }

        if (session.getSoloChallenge() == null) {
            throw new IllegalArgumentException(
                    "Resume is only available for solo workouts."
            );
        }

        if (
                session.getStatus()
                        != FitnessWorkoutStatus.PAUSED
        ) {
            throw new IllegalArgumentException(
                    "Only a paused workout can be resumed."
            );
        }

        if (session.getPausedAt() == null) {
            throw new IllegalStateException(
                    "Paused time was not recorded."
            );
        }

        LocalDateTime now =
                LocalDateTime.now();

        long pausedSeconds =
                java.time.Duration
                        .between(
                                session.getPausedAt(),
                                now
                        )
                        .getSeconds();

        int currentPausedSeconds =
                session.getTotalPausedSeconds() != null
                        ? session.getTotalPausedSeconds()
                        : 0;

        session.setTotalPausedSeconds(
                currentPausedSeconds
                        + (int) pausedSeconds
        );

        session.setPausedAt(null);

        session.setStatus(
                FitnessWorkoutStatus.IN_PROGRESS
        );

        session.setUpdatedAt(now);

        workoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public FitnessWorkoutSessionResponse updateProgress(
            Integer sessionId,
            WorkoutProgressRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (
                !session.getUser()
                        .getId()
                        .equals(currentUser.getId())
        ) {

            throw new IllegalArgumentException(
                    "You can only update your own workout session."
            );
        }


        if (
                session.getStatus()
                        != FitnessWorkoutStatus.IN_PROGRESS
        ) {

            throw new IllegalArgumentException(
                    "Only an active workout session can be updated."
            );
        }


        session.setDurationSeconds(
                request.getDurationSeconds()
        );

        session.setSteps(
                request.getSteps()
        );

        session.setDistance(
                request.getDistance()
        );

        session.setUpdatedAt(
                LocalDateTime.now()
        );

        FitnessWorkoutSession saved =
                workoutSessionRepository.save(
                        session
                );

        return workoutSessionMapper.toResponse(
                saved
        );
    }

    @Override
    @Transactional
    public FitnessWorkoutSessionResponse finishWorkout(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUserAndActivity_StatusNot(
                                sessionId,
                                currentUser,
                                ActivityStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (
                session.getStatus()
                        != FitnessWorkoutStatus.IN_PROGRESS
        ) {

            throw new IllegalArgumentException(
                    "Only an active workout session can be finished."
            );
        }

        LocalDateTime now =
                LocalDateTime.now();

        int activeDurationSeconds =
                calculateCurrentActiveDuration(
                        session,
                        now
                );

        session.setDurationSeconds(
                activeDurationSeconds
        );

        boolean gpsWorkout =
                session.getSoloChallenge() != null;

        if (gpsWorkout) {

            List<FitnessWorkoutRoutePoint> routePoints =
                    workoutRoutePointRepository
                            .findByWorkoutSessionOrderByPointSequenceAsc(
                                    session
                            );

            List<FitnessWorkoutRoutePoint> validPoints =
                    routePoints.stream()
                            .filter(this::hasValidCoordinates)
                            .filter(this::hasAcceptableAccuracy)
                            .toList();

            if (!routePoints.isEmpty()) {

                BigDecimal finalDistance =
                        workoutRouteCalculationService
                                .calculateDistance(
                                        routePoints
                                );

                session.setDistance(
                        finalDistance
                );

                BigDecimal finalSpeed =
                        workoutRouteCalculationService
                                .calculateSpeed(
                                        finalDistance,
                                        activeDurationSeconds
                                );

                session.setAverageSpeed(
                        finalSpeed
                );

                BigDecimal finalPace =
                        workoutRouteCalculationService
                                .calculatePace(
                                        finalDistance,
                                        activeDurationSeconds
                                );

                session.setAveragePace(
                        finalPace
                );
            }
        }


        session.setFinishedAt(
                now
        );

        session.setStatus(
                FitnessWorkoutStatus.COMPLETED
        );

        Activity activity =
                session.getActivity();

        if (activity != null) {

            activity.setStatus(
                    ActivityStatus.COMPLETE
            );

            activity.setUpdatedAt(
                    now
            );

            activityRepository.save(
                    activity
            );
        }

        BigDecimal calories =
                calorieCalculationService.calculateCalories(
                        currentUser,
                        session
                );

        session.setCaloriesBurned(
                calories
        );

        FitnessChallengeParticipant participant =
                session.getGroupChallengeParticipant();

        if (
                participant != null &&
                        participant.getStatus()
                                == FitnessChallengeParticipantStatus.ACTIVE
        ) {

            participant.setStatus(
                    FitnessChallengeParticipantStatus.COMPLETED
            );

            participant.setCompletedAt(
                    now
            );

            participantRepository.save(
                    participant
            );
        }

        session.setUpdatedAt(
                now
        );

        FitnessWorkoutSession saved =
                workoutSessionRepository.save(
                        session
                );

        return workoutSessionMapper.toResponse(
                saved
        );
    }

    @Override
    @Transactional
    public List<WorkoutHistoryResponse> getWorkoutHistory() {

        User currentUser =
                currentUserService.getCurrentUser();

        return workoutSessionRepository
                .findByUserAndStatusAndActivity_StatusNotOrderByFinishedAtDesc(
                        currentUser,
                        FitnessWorkoutStatus.COMPLETED,
                        ActivityStatus.DELETED
                )
                .stream()
                .map(workoutSessionMapper::toHistoryResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteWorkout(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUserAndActivity_StatusNot(sessionId,currentUser,ActivityStatus.DELETED)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (
                session.getStatus()
                        == FitnessWorkoutStatus.IN_PROGRESS
                        ||
                        session.getStatus()
                                == FitnessWorkoutStatus.PAUSED
        ) {

            throw new IllegalArgumentException(
                    "An active workout cannot be deleted."
            );
        }

        Activity activity =
                session.getActivity();

        if (activity == null) {
            throw new IllegalStateException(
                    "Workout session is not linked to an activity."
            );
        }

        LocalDateTime now =
                LocalDateTime.now();

        activity.setStatus(
                ActivityStatus.DELETED
        );

        activity.setDeletedAt(now);
        activity.setUpdatedAt(now);

        activityRepository.save(activity);

        session.setUpdatedAt(now);

        workoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public FitnessWorkoutSessionResponse getSession(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUserAndActivity_StatusNot(
                                sessionId,
                                currentUser,
                                ActivityStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        return workoutSessionMapper.toResponse(session);
    }


    @Override
    @Transactional
    public List<FitnessWorkoutSessionResponse> getMySessions() {

        User currentUser =
                currentUserService.getCurrentUser();

        return workoutSessionRepository
                .findByUserAndActivity_StatusNot(
                        currentUser,
                        ActivityStatus.DELETED
                )
                .stream()
                .map(workoutSessionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public WorkoutDetailsResponse getWorkoutDetails(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUserAndActivity_StatusNot(
                                sessionId,
                                currentUser,
                                ActivityStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        Integer durationSeconds =
                session.getDurationSeconds() != null
                        ? session.getDurationSeconds()
                        : 0;

        BigDecimal distance =
                session.getDistance() != null
                        ? session.getDistance()
                        : BigDecimal.ZERO;

        BigDecimal calories =
                session.getCaloriesBurned() != null
                        ? session.getCaloriesBurned()
                        : BigDecimal.ZERO;

        BigDecimal averageSpeed =
                session.getAverageSpeed() != null
                        ? session.getAverageSpeed()
                        : BigDecimal.ZERO;

        BigDecimal averagePace =
                session.getAveragePace();

        BigDecimal caloriesPerMinute =
                BigDecimal.ZERO;

        if (durationSeconds > 0) {

            caloriesPerMinute =
                    calories
                            .divide(
                                    BigDecimal.valueOf(
                                            durationSeconds
                                    ),
                                    6,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(
                                    BigDecimal.valueOf(60)
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );
        }

        /*
         * Challenge information
         */
        WorkoutChallengeDetailsResponse challenge = null;

        if (session.getSoloChallenge() != null) {

            SoloChallenge soloChallenge =
                    session.getSoloChallenge();

            challenge =
                    WorkoutChallengeDetailsResponse.builder()
                            .type("SOLO")
                            .id(soloChallenge.getId())
                            .participantId(null)
                            .name(soloChallenge.getName())
                            .targetValue(
                                    soloChallenge.getTargetValue()
                            )
                            .targetUnit(
                                    soloChallenge.getTargetUnit() != null
                                            ? soloChallenge
                                            .getTargetUnit()
                                            .name()
                                            : null
                            )
                            .build();
        }

        else if (
                session.getGroupChallengeParticipant() != null
        ) {

            FitnessChallengeParticipant participant =
                    session.getGroupChallengeParticipant();

            GroupFitnessChallenge groupChallenge =
                    participant.getChallenge();

            if (groupChallenge != null) {

                challenge =
                        WorkoutChallengeDetailsResponse.builder()
                                .type("GROUP")
                                .id(groupChallenge.getId())
                                .participantId(
                                        participant.getId()
                                )
                                .name(
                                        groupChallenge.getName()
                                )
                                .targetValue(
                                        groupChallenge
                                                .getTargetValue()
                                )
                                .targetUnit(
                                        groupChallenge
                                                .getTargetUnit() != null
                                                ? groupChallenge
                                                .getTargetUnit()
                                                .name()
                                                : null
                                )
                                .build();
            }
        }

        /*
         * User's overall workout totals
         */
        List<FitnessWorkoutSession> completedWorkouts =
                workoutSessionRepository
                        .findByUserAndStatusAndActivity_StatusNotOrderByFinishedAtDesc(
                                currentUser,
                                FitnessWorkoutStatus.COMPLETED,
                                ActivityStatus.DELETED
                        );

        int totalCompletedWorkouts =
                completedWorkouts.size();

        BigDecimal totalDistance =
                completedWorkouts.stream()
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

        BigDecimal totalCalories =
                completedWorkouts.stream()
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

        long totalWorkoutSeconds =
                completedWorkouts.stream()
                        .mapToLong(
                                workout ->
                                        workout.getDurationSeconds() != null
                                                ? workout.getDurationSeconds()
                                                : 0
                        )
                        .sum();

        String formattedPace = formatPace(
                session.getAveragePace()
        );

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
                .build();
    }

    //GPS ROUTE

    @Override
    @Transactional
    public void addRoutePoints(
            Integer sessionId,
            WorkoutRoutePointsRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUserAndActivity_StatusNot(
                                sessionId,
                                currentUser,
                                ActivityStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (
                session.getStatus()
                        != FitnessWorkoutStatus.IN_PROGRESS
        ) {
            throw new IllegalArgumentException(
                    "GPS points can only be added to an active workout."
            );
        }

        if (session.getSoloChallenge() == null) {
            throw new IllegalArgumentException(
                    "GPS route tracking is currently available only for solo workouts."
            );
        }

        LocalDateTime now =
                LocalDateTime.now();

        List<FitnessWorkoutRoutePoint> existingPoints =
                workoutRoutePointRepository
                        .findByWorkoutSessionOrderByPointSequenceAsc(
                                session
                        );

        FitnessWorkoutRoutePoint previousPoint =
                existingPoints.isEmpty()
                        ? null
                        : existingPoints.get(
                        existingPoints.size() - 1
                );

        for (
                WorkoutRoutePointsRequest.RoutePointRequest pointRequest
                : request.getPoints()
        ) {

            if (
                    pointRequest.getLatitude() == null ||
                            pointRequest.getLongitude() == null ||
                            pointRequest.getRecordedAt() == null
            ) {
                throw new IllegalArgumentException(
                        "Latitude, longitude and recordedAt are required."
                );
            }

            FitnessWorkoutRoutePoint point =
                    new FitnessWorkoutRoutePoint();

            point.setWorkoutSession(session);

            point.setPointSequence(
                    pointRequest.getPointSequence()
            );

            point.setLatitude(
                    pointRequest.getLatitude()
            );

            point.setLongitude(
                    pointRequest.getLongitude()
            );

            point.setAccuracy(
                    pointRequest.getAccuracy()
            );

            point.setAltitude(
                    pointRequest.getAltitude()
            );

            point.setRecordedAt(
                    pointRequest.getRecordedAt()
            );

            // Validate before saving
            if (!hasValidCoordinates(point)) {
                continue;
            }

            if (!hasAcceptableAccuracy(point)) {
                continue;
            }

            if (!hasValidSequence(point, previousPoint)) {
                continue;
            }

            if (!hasValidTimestamp(point, previousPoint)) {
                continue;
            }

            if (!isReasonableMovement(previousPoint, point)) {
                continue;
            }

            workoutRoutePointRepository.save(point);

            // Last accepted point becomes reference
            previousPoint = point;
        }

        List<FitnessWorkoutRoutePoint> validPoints =
                workoutRoutePointRepository
                        .findByWorkoutSessionOrderByPointSequenceAsc(
                                session
                        );

        BigDecimal distance =
                workoutRouteCalculationService.calculateDistance(
                        validPoints
                );

        int activeDurationSeconds =
                calculateCurrentActiveDuration(
                        session,
                        now
                );

        BigDecimal speed =
                workoutRouteCalculationService.calculateSpeed(
                        distance,
                        activeDurationSeconds
                );

        BigDecimal pace =
                workoutRouteCalculationService.calculatePace(
                        distance,
                        activeDurationSeconds
                );

        session.setDistance(distance);

        session.setDurationSeconds(
                activeDurationSeconds
        );

        session.setAverageSpeed(
                speed
        );

        session.setAveragePace(
                pace
        );

        session.setUpdatedAt(now);

        workoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public List<WorkoutRoutePointResponse> getWorkoutRoute(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUserAndActivity_StatusNot(
                                sessionId,
                                currentUser,
                                ActivityStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        return workoutRoutePointRepository
                .findByWorkoutSessionOrderByPointSequenceAsc(
                        session
                )
                .stream()
                .map(point ->
                        WorkoutRoutePointResponse.builder()
                                .id(point.getId())
                                .pointSequence(
                                        point.getPointSequence()
                                )
                                .latitude(
                                        point.getLatitude()
                                )
                                .longitude(
                                        point.getLongitude()
                                )
                                .accuracy(
                                        point.getAccuracy()
                                )
                                .altitude(
                                        point.getAltitude()
                                )
                                .recordedAt(
                                        point.getRecordedAt()
                                )
                                .build()
                )
                .toList();
    }

    private boolean isValidRoutePoint(
            FitnessWorkoutRoutePoint point
    ) {

        BigDecimal latitude = point.getLatitude();
        BigDecimal longitude = point.getLongitude();

        if (latitude == null || longitude == null) {
            return false;
        }

        if (
                latitude.compareTo(BigDecimal.valueOf(-90)) < 0 ||
                        latitude.compareTo(BigDecimal.valueOf(90)) > 0
        ) {
            return false;
        }

        if (
                longitude.compareTo(BigDecimal.valueOf(-180)) < 0 ||
                        longitude.compareTo(BigDecimal.valueOf(180)) > 0
        ) {
            return false;
        }

        if (
                point.getAccuracy() != null &&
                        point.getAccuracy().compareTo(
                                BigDecimal.valueOf(50)
                        ) > 0
        ) {
            return false;
        }

        return true;
    }

    private int calculateCurrentActiveDuration(
            FitnessWorkoutSession session,
            LocalDateTime now
    ) {

        if (session.getStartedAt() == null) {
            return 0;
        }

        long elapsedSeconds =
                java.time.Duration
                        .between(
                                session.getStartedAt(),
                                now
                        )
                        .getSeconds();

        long pausedSeconds =
                session.getTotalPausedSeconds() != null
                        ? session.getTotalPausedSeconds()
                        : 0;

        /*
         * If the workout is currently paused,
         * the current pause interval must also be excluded.
         */
        if (
                session.getStatus()
                        == FitnessWorkoutStatus.PAUSED
                        &&
                        session.getPausedAt() != null
        ) {

            pausedSeconds +=
                    java.time.Duration
                            .between(
                                    session.getPausedAt(),
                                    now
                            )
                            .getSeconds();
        }

        return (int) Math.max(
                0,
                elapsedSeconds - pausedSeconds
        );
    }


    // Helper Method

    private String formatPace(BigDecimal secondsPerKm) {

        if (
                secondsPerKm == null ||
                        secondsPerKm.compareTo(BigDecimal.ZERO) <= 0
        ) {
            return null;
        }

        long totalSeconds =
                secondsPerKm
                        .setScale(
                                0,
                                RoundingMode.HALF_UP
                        )
                        .longValue();

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format(
                "%d:%02d",
                minutes,
                seconds
        );
    }

    private boolean hasValidCoordinates(
            FitnessWorkoutRoutePoint point
    ) {

        if (
                point.getLatitude() == null ||
                        point.getLongitude() == null
        ) {
            return false;
        }

        return point.getLatitude()
                .compareTo(BigDecimal.valueOf(-90)) >= 0
                &&
                point.getLatitude()
                        .compareTo(BigDecimal.valueOf(90)) <= 0
                &&
                point.getLongitude()
                        .compareTo(BigDecimal.valueOf(-180)) >= 0
                &&
                point.getLongitude()
                        .compareTo(BigDecimal.valueOf(180)) <= 0;
    }

    private boolean hasAcceptableAccuracy(
            FitnessWorkoutRoutePoint point
    ) {

        if (point.getAccuracy() == null) {
            return true;
        }

        return point.getAccuracy()
                .compareTo(BigDecimal.valueOf(50)) <= 0;
    }

    private boolean hasValidSequence(
            FitnessWorkoutRoutePoint point,
            FitnessWorkoutRoutePoint previousPoint
    ) {

        if (point.getPointSequence() == null) {
            return false;
        }

        if (previousPoint == null) {
            return true;
        }

        return point.getPointSequence()
                > previousPoint.getPointSequence();
    }

    private boolean hasValidTimestamp(
            FitnessWorkoutRoutePoint point,
            FitnessWorkoutRoutePoint previousPoint
    ) {

        if (point.getRecordedAt() == null) {
            return false;
        }

        if (previousPoint == null) {
            return true;
        }

        return !point.getRecordedAt()
                .isBefore(
                        previousPoint.getRecordedAt()
                );
    }

    private boolean isReasonableMovement(
            FitnessWorkoutRoutePoint previousPoint,
            FitnessWorkoutRoutePoint currentPoint
    ) {

        if (
                previousPoint == null ||
                        currentPoint == null
        ) {
            return true;
        }

        if (
                previousPoint.getRecordedAt() == null ||
                        currentPoint.getRecordedAt() == null
        ) {
            return false;
        }

        long elapsedSeconds =
                java.time.Duration.between(
                        previousPoint.getRecordedAt(),
                        currentPoint.getRecordedAt()
                ).getSeconds();

        if (elapsedSeconds <= 0) {
            return false;
        }

        BigDecimal segmentDistance =
                calculateSegmentDistance(
                        previousPoint,
                        currentPoint
                );

        double distanceKm =
                segmentDistance.doubleValue();

        double speedKmh =
                distanceKm /
                        (elapsedSeconds / 3600.0);

        /*
         * Development threshold.
         * We can tune this after real device testing.
         */
        return speedKmh <= 40.0;
    }

    private BigDecimal calculateSegmentDistance(
            FitnessWorkoutRoutePoint first,
            FitnessWorkoutRoutePoint second
    ) {

        final double earthRadiusKm = 6371.0;

        double lat1 =
                Math.toRadians(
                        first.getLatitude().doubleValue()
                );

        double lon1 =
                Math.toRadians(
                        first.getLongitude().doubleValue()
                );

        double lat2 =
                Math.toRadians(
                        second.getLatitude().doubleValue()
                );

        double lon2 =
                Math.toRadians(
                        second.getLongitude().doubleValue()
                );

        double deltaLat =
                lat2 - lat1;

        double deltaLon =
                lon2 - lon1;

        double a =
                Math.sin(deltaLat / 2)
                        * Math.sin(deltaLat / 2)
                        +
                        Math.cos(lat1)
                                * Math.cos(lat2)
                                * Math.sin(deltaLon / 2)
                                * Math.sin(deltaLon / 2);

        double c =
                2 *
                        Math.atan2(
                                Math.sqrt(a),
                                Math.sqrt(1 - a)
                        );

        return BigDecimal.valueOf(
                earthRadiusKm * c
        );
    }
}
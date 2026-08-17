package com.movem.backend.Service.Implement.FitnessServices.Workout;

import com.movem.backend.Dto.request.FitnessRequest.Workout.StartWorkoutRequest;
import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutProgressRequest;
import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutRoutePointsRequest;
import com.movem.backend.Dto.response.FitnessResponse.Workout.FitnessWorkoutSessionResponse;
import com.movem.backend.Dto.response.FitnessResponse.Workout.WorkoutDetailsResponse;
import com.movem.backend.Dto.response.FitnessResponse.Workout.WorkoutHistoryResponse;
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
                    "You can only finish your own workout session."
            );
        }

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

        long totalElapsedSeconds =
                java.time.Duration
                        .between(
                                session.getStartedAt(),
                                now
                        )
                        .getSeconds();

        long totalPausedSeconds =
                session.getTotalPausedSeconds() != null
                        ? session.getTotalPausedSeconds()
                        : 0;

        long activeDuration =
                Math.max(
                        0,
                        totalElapsedSeconds
                                - totalPausedSeconds
                );

        session.setDurationSeconds(
                (int) activeDuration
        );

        session.setFinishedAt(now);

        session.setStatus(
                FitnessWorkoutStatus.COMPLETED
        );

        Activity activity =
                session.getActivity();

        if (activity != null) {

            activity.setStatus(
                    ActivityStatus.COMPLETE
            );

            activity.setUpdatedAt(now);

            activityRepository.save(activity);
        }

        if (
                session.getDistance() != null &&
                        session.getDistance()
                                .compareTo(BigDecimal.ZERO) > 0
        ) {

            BigDecimal pace =
                    BigDecimal.valueOf(
                                    session.getDurationSeconds()
                            )
                            .divide(
                                    session.getDistance(),
                                    2,
                                    RoundingMode.HALF_UP
                            );

            session.setAveragePace(
                    pace
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

            participant.setCompletedAt(now);

            participantRepository.save(
                    participant
            );
        }


        session.setUpdatedAt(now);


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
                        .findByIdAndUserAndActivity_StatusNot(sessionId,currentUser,ActivityStatus.DELETED)
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
                BigDecimal.ZERO;

        if (
                durationSeconds > 0 &&
                        distance.compareTo(BigDecimal.ZERO) > 0
        ) {

            averageSpeed =
                    distance
                            .divide(
                                    BigDecimal.valueOf(
                                            durationSeconds
                                    ),
                                    6,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(
                                    BigDecimal.valueOf(3600)
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );
        }

        /*
         * Calories per minute
         */
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
         * Determine challenge context
         */
        Integer soloChallengeId = null;
        Integer groupChallengeId = null;
        Integer groupParticipantId = null;

        String challengeName = null;
        BigDecimal challengeTargetValue = null;
        String challengeTargetUnit = null;


        if (session.getSoloChallenge() != null) {

            soloChallengeId =
                    session.getSoloChallenge().getId();

            challengeName =
                    session.getSoloChallenge().getName();

            challengeTargetValue =
                    session.getSoloChallenge().getTargetValue();

            challengeTargetUnit =
                    session.getSoloChallenge()
                            .getTargetUnit()
                            .name();
        }


        if (
                session.getGroupChallengeParticipant()
                        != null
        ) {

            FitnessChallengeParticipant participant =
                    session.getGroupChallengeParticipant();

            groupParticipantId =
                    participant.getId();

            if (participant.getChallenge() != null) {

                GroupFitnessChallenge challenge =
                        participant.getChallenge();

                groupChallengeId =
                        challenge.getId();

                challengeName =
                        challenge.getName();

                challengeTargetValue =
                        challenge.getTargetValue();

                challengeTargetUnit =
                        challenge.getTargetUnit().name();
            }
        }

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


        /*
         * Previous workout
         */
        FitnessWorkoutSession previousWorkout =
                completedWorkouts.stream()
                        .filter(
                                workout ->
                                        !workout.getId()
                                                .equals(session.getId())
                        )
                        .findFirst()
                        .orElse(null);


        Integer previousWorkoutId = null;
        BigDecimal distanceChange = null;
        BigDecimal calorieChange = null;


        if (previousWorkout != null) {

            previousWorkoutId =
                    previousWorkout.getId();

            BigDecimal previousDistance =
                    previousWorkout.getDistance() != null
                            ? previousWorkout.getDistance()
                            : BigDecimal.ZERO;

            BigDecimal previousCalories =
                    previousWorkout.getCaloriesBurned() != null
                            ? previousWorkout.getCaloriesBurned()
                            : BigDecimal.ZERO;

            distanceChange =
                    distance
                            .subtract(previousDistance)
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );

            calorieChange =
                    calories
                            .subtract(previousCalories)
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );
        }


        return WorkoutDetailsResponse.builder()

                .id(session.getId())

                .workoutType(
                        session.getWorkoutType()
                )

                .status(
                        session.getStatus()
                )

                .startedAt(
                        session.getStartedAt()
                )

                .finishedAt(
                        session.getFinishedAt()
                )

                .durationSeconds(
                        durationSeconds
                )

                .totalPausedSeconds(
                        session.getTotalPausedSeconds()
                )

                .steps(
                        session.getSteps()
                )

                .distance(
                        distance
                )

                .caloriesBurned(
                        calories
                )

                .averagePace(
                        session.getAveragePace()
                )

                .averageSpeed(
                        averageSpeed
                )

                .caloriesPerMinute(
                        caloriesPerMinute
                )

                .soloChallengeId(
                        soloChallengeId
                )

                .groupChallengeId(
                        groupChallengeId
                )

                .groupParticipantId(
                        groupParticipantId
                )

                .challengeName(
                        challengeName
                )

                .challengeTargetValue(
                        challengeTargetValue
                )

                .challengeTargetUnit(
                        challengeTargetUnit
                )

                .totalCompletedWorkouts(
                        totalCompletedWorkouts
                )

                .totalDistance(
                        totalDistance
                )

                .totalCaloriesBurned(
                        totalCalories
                )

                .totalWorkoutSeconds(
                        totalWorkoutSeconds
                )

                .previousWorkoutId(
                        previousWorkoutId
                )

                .distanceChange(
                        distanceChange
                )

                .calorieChange(
                        calorieChange
                )

                .build();
    }

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

        // Only an active workout can receive GPS points.
        if (
                session.getStatus()
                        != FitnessWorkoutStatus.IN_PROGRESS
        ) {
            throw new IllegalArgumentException(
                    "GPS points can only be added to an active workout."
            );
        }

        // Group workouts currently don't use live GPS tracking.
        if (
                session.getSoloChallenge() == null
        ) {
            throw new IllegalArgumentException(
                    "GPS route tracking is currently available only for solo workouts."
            );
        }

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

            workoutRoutePointRepository.save(point);
        }
    }


}
package com.movem.backend.Service.Implement.FitnessServices.Workout;

import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutRoutePointsRequest;
import com.movem.backend.Dto.response.FitnessResponse.Workout.FitnessWorkoutRoutePointResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutRoutePoint;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutRoutePointRepository;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutSessionRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.Workout.FitnessWorkoutRouteService;
import com.movem.backend.Service.FitnessServices.Workout.WorkoutRouteCalculationService;
import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.model.enums.Fitness.TrackingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessWorkoutRouteServiceImpl
        implements FitnessWorkoutRouteService {

    private final FitnessWorkoutRoutePointRepository routePointRepository;
    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final WorkoutRouteCalculationService workoutRouteCalculationService;
    private final CurrentUserService currentUserService;

    @Override
    public void addRoutePoints(
            Integer sessionId,
            WorkoutRoutePointsRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUser(
                                sessionId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (session.getTrackingMode() != TrackingMode.GPS) {
            throw new IllegalArgumentException(
                    "Workout session does not use GPS tracking."
            );
        }

        if (session.getStatus() != FitnessWorkoutStatus.IN_PROGRESS) {
            throw new IllegalArgumentException(
                    "Route points can only be added to an active workout."
            );
        }

        for (
                WorkoutRoutePointsRequest.RoutePointRequest pointRequest
                : request.getPoints()
        ) {

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
                    pointRequest.getRecordedAt() != null
                            ? pointRequest.getRecordedAt()
                            : LocalDateTime.now()
            );

            routePointRepository.save(point);
        }

        List<FitnessWorkoutRoutePoint> routePoints =
                routePointRepository
                        .findByWorkoutSessionOrderByPointSequenceAsc(
                                session
                        );

        BigDecimal distance =
                workoutRouteCalculationService
                        .calculateDistance(routePoints);

        session.setDistance(distance);
        session.setUpdatedAt(LocalDateTime.now());

        workoutSessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FitnessWorkoutRoutePointResponse> getRoute(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUser(
                                sessionId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        return routePointRepository
                .findByWorkoutSessionOrderByPointSequenceAsc(
                        session
                )
                .stream()
                .map(point ->
                        FitnessWorkoutRoutePointResponse.builder()
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
}
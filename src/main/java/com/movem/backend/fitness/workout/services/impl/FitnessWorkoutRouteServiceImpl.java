package com.movem.backend.fitness.workout.services.impl;

import com.movem.backend.fitness.workout.dtos.requests.WorkoutRoutePointsRequest;
import com.movem.backend.fitness.workout.dtos.responses.FitnessWorkoutRoutePointResponse;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutRoutePoint;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutRoutePointRepository;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutSessionRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.fitness.workout.services.FitnessWorkoutRouteService;
import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessWorkoutRouteServiceImpl implements FitnessWorkoutRouteService {
    private final FitnessWorkoutRoutePointRepository routePointRepository;
    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final CurrentUserService currentUserService;

    @Transactional
    @Override
    public void addRoutePoints(Integer sessionId, WorkoutRoutePointsRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository.findByIdAndUser(sessionId, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        if (session.getStatus() != FitnessWorkoutStatus.IN_PROGRESS && session.getStatus() != FitnessWorkoutStatus.PAUSED) {
            throw new IllegalArgumentException("Route points can only be added while the workout is active.");
        }

        List<FitnessWorkoutRoutePoint> routePoints =
                request.getPoints()
                        .stream().map(point -> {

                            FitnessWorkoutRoutePoint routePoint = new FitnessWorkoutRoutePoint();
                            routePoint.setWorkoutSession(session);
                            routePoint.setPointSequence(point.getPointSequence());
                            routePoint.setLatitude(point.getLatitude());
                            routePoint.setLongitude(point.getLongitude());
                            routePoint.setAccuracy(point.getAccuracy());
                            routePoint.setAltitude(point.getAltitude());
                            routePoint.setRecordedAt(point.getRecordedAt());

                            return routePoint;}).toList();

        routePointRepository.saveAll(routePoints);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FitnessWorkoutRoutePointResponse> getRoute(Integer sessionId) {

        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository
                        .findByIdAndUser(sessionId, currentUser)
                        .orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        return routePointRepository.findByWorkoutSessionOrderByPointSequenceAsc(session)
                .stream()
                .map(point ->
                        FitnessWorkoutRoutePointResponse.builder()
                                .id(point.getId())
                                .pointSequence(point.getPointSequence())
                                .latitude(point.getLatitude())
                                .longitude(point.getLongitude())
                                .accuracy(point.getAccuracy())
                                .altitude(point.getAltitude())
                                .recordedAt(point.getRecordedAt())
                                .build()).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FitnessWorkoutRoutePointResponse> getRoute(FitnessWorkoutSession session) {
        return routePointRepository.findByWorkoutSessionOrderByPointSequenceAsc(session)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private FitnessWorkoutRoutePointResponse toResponse(FitnessWorkoutRoutePoint point) {
        return FitnessWorkoutRoutePointResponse.builder()
                .id(point.getId())
                .pointSequence(point.getPointSequence())
                .latitude(point.getLatitude())
                .longitude(point.getLongitude())
                .accuracy(point.getAccuracy())
                .altitude(point.getAltitude())
                .recordedAt(point.getRecordedAt())
                .build();
    }
}
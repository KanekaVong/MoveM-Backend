package com.movem.backend.fitness.workout.services;

import com.movem.backend.fitness.workout.dtos.requests.WorkoutRoutePointsRequest;
import com.movem.backend.fitness.workout.dtos.responses.FitnessWorkoutRoutePointResponse;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;

import java.util.List;

public interface FitnessWorkoutRouteService {
    @jakarta.transaction.Transactional
    void addRoutePoints(Integer sessionId, WorkoutRoutePointsRequest request);
    List<FitnessWorkoutRoutePointResponse> getRoute(Integer sessionId);
    List<FitnessWorkoutRoutePointResponse> getRoute(FitnessWorkoutSession session);
}
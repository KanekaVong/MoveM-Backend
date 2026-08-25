package com.movem.backend.Service.FitnessServices.Workout;

import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutRoutePointsRequest;
import com.movem.backend.Dto.response.FitnessResponse.Workout.FitnessWorkoutRoutePointResponse;

import java.util.List;

public interface FitnessWorkoutRouteService {

    void addRoutePoints(
            Integer sessionId,
            WorkoutRoutePointsRequest request
    );

    List<FitnessWorkoutRoutePointResponse> getRoute(
            Integer sessionId
    );
}
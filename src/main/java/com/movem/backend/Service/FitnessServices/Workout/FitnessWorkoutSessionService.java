package com.movem.backend.Service.FitnessServices.Workout;

import com.movem.backend.Dto.request.FitnessRequest.Workout.StartWorkoutRequest;
import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutProgressRequest;
import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutRoutePointsRequest;
import com.movem.backend.Dto.response.FitnessResponse.Workout.FitnessWorkoutSessionResponse;
import com.movem.backend.Dto.response.FitnessResponse.Workout.WorkoutDetailsResponse;
import com.movem.backend.Dto.response.FitnessResponse.Workout.WorkoutHistoryResponse;

import java.util.List;

public interface FitnessWorkoutSessionService {

    FitnessWorkoutSessionResponse startWorkout(
            StartWorkoutRequest request
    );

    void pauseWorkout(
            Integer sessionId
    );

    void resumeWorkout(
            Integer sessionId
    );

    FitnessWorkoutSessionResponse getSession(
            Integer sessionId
    );

    List<FitnessWorkoutSessionResponse> getMySessions();

    FitnessWorkoutSessionResponse updateProgress(
            Integer sessionId,
            WorkoutProgressRequest request
    );

    FitnessWorkoutSessionResponse finishWorkout(
            Integer sessionId
    );

    List<WorkoutHistoryResponse> getWorkoutHistory();

    WorkoutDetailsResponse getWorkoutDetails(
            Integer sessionId
    );

    void deleteWorkout(Integer sessionId);

    void addRoutePoints(
            Integer sessionId,
            WorkoutRoutePointsRequest request
    );
}
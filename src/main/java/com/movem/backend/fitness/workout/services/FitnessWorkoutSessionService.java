package com.movem.backend.fitness.workout.services;

import com.movem.backend.fitness.workout.dtos.responses.SocialWorkoutResponse;
import com.movem.backend.fitness.workout.dtos.requests.FinishWorkoutRequest;
import com.movem.backend.fitness.workout.dtos.requests.FitnessWorkoutSearchRequest;
import com.movem.backend.fitness.workout.dtos.requests.ShareWorkoutRequest;
import com.movem.backend.fitness.workout.dtos.requests.StartWorkoutRequest;
import com.movem.backend.fitness.workout.dtos.responses.*;

import java.util.List;

public interface FitnessWorkoutSessionService {
    FitnessWorkoutSessionResponse startWorkout(StartWorkoutRequest request);

    void pauseWorkout(Integer sessionId);
    void resumeWorkout(Integer sessionId);

    FitnessWorkoutSessionResponse getSession(Integer sessionId);

    List<FitnessWorkoutSessionResponse> getMySessions();

    FitnessWorkoutSessionResponse finishWorkout(Integer sessionId, FinishWorkoutRequest request);

    List<WorkoutHistoryResponse> getWorkoutHistory();

    WorkoutDetailsResponse getWorkoutDetails(Integer sessionId);

    void deleteWorkout(Integer sessionId);

    //GPS ROUTE

    List<WorkoutRoutePointResponse> getWorkoutRoute(Integer sessionId);

    SocialWorkoutResponse getSocialWorkout(Integer sessionId);

    FitnessWorkoutSummaryResponse getWorkoutSummary(Integer sessionId);

    void updateWorkoutSharing(Integer sessionId, ShareWorkoutRequest request);

    List<SharedWorkoutPostResponse> getSocialWorkoutFeed();

    List<WorkoutHistoryResponse> searchWorkouts(FitnessWorkoutSearchRequest request);
}
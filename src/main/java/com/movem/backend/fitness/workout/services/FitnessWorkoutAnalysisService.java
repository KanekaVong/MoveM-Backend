package com.movem.backend.fitness.workout.services;

import com.movem.backend.fitness.workout.dtos.requests.FitnessWorkoutAnalysisRequest;
import com.movem.backend.fitness.workout.dtos.responses.FitnessWorkoutAnalysisResponse;

public interface FitnessWorkoutAnalysisService {

    FitnessWorkoutAnalysisResponse saveAnalysis(
            Integer sessionId,
            FitnessWorkoutAnalysisRequest request
    );

    FitnessWorkoutAnalysisResponse getAnalysis(
            Integer sessionId
    );
}
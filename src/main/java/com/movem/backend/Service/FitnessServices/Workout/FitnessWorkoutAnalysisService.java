package com.movem.backend.Service.FitnessServices.Workout;

import com.movem.backend.Dto.request.FitnessRequest.Workout.FitnessWorkoutAnalysisRequest;
import com.movem.backend.Dto.response.FitnessResponse.Workout.FitnessWorkoutAnalysisResponse;

public interface FitnessWorkoutAnalysisService {

    FitnessWorkoutAnalysisResponse saveAnalysis(
            Integer sessionId,
            FitnessWorkoutAnalysisRequest request
    );

    FitnessWorkoutAnalysisResponse getAnalysis(
            Integer sessionId
    );
}
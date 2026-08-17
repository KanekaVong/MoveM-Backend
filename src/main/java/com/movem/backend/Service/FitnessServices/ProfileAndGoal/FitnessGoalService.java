package com.movem.backend.Service.FitnessServices.ProfileAndGoal;

import com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal.CreateFitnessGoalRequest;
import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessGoalResponse;

import java.util.List;

public interface FitnessGoalService {

    FitnessGoalResponse createGoal(
            CreateFitnessGoalRequest request
    );

    FitnessGoalResponse getGoal(
            Integer goalId
    );

    List<FitnessGoalResponse> getMyGoals();

    FitnessGoalResponse updateGoal(
            Integer goalId,
            CreateFitnessGoalRequest request
    );

    void deleteGoal(
            Integer goalId
    );
}

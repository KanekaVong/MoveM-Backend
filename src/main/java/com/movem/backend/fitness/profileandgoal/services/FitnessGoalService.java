package com.movem.backend.fitness.profileandgoal.services;

import com.movem.backend.fitness.profileandgoal.dtos.requests.CreateFitnessGoalRequest;
import com.movem.backend.fitness.profileandgoal.dtos.responses.FitnessGoalResponse;

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

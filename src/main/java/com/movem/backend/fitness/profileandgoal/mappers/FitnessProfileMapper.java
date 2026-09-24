package com.movem.backend.fitness.profileandgoal.mappers;

import com.movem.backend.fitness.profileandgoal.dtos.responses.FitnessGoalResponse;
import com.movem.backend.fitness.profileandgoal.dtos.responses.FitnessProfileResponse;
import com.movem.backend.fitness.profileandgoal.entities.FitnessProfile;
import org.springframework.stereotype.Component;

@Component
public class FitnessProfileMapper {

    public FitnessProfileResponse toResponse(
            FitnessProfile fitnessProfile,
            FitnessGoalResponse fitnessGoal
    ) {

        return FitnessProfileResponse.builder()
                .userId(fitnessProfile.getUserId())
                .height(fitnessProfile.getHeight())
                .weight(fitnessProfile.getWeight())
                .bmi(fitnessProfile.getBmi())
                .fitnessGoal(fitnessGoal)
                .updatedAt(fitnessProfile.getUpdatedAt())
                .build();
    }
}
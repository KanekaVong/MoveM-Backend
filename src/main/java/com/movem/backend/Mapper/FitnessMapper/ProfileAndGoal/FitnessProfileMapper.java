package com.movem.backend.Mapper.FitnessMapper.ProfileAndGoal;

import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessProfileResponse;
import com.movem.backend.Entity.Fitness.ProfileAndGoal.FitnessProfile;
import org.springframework.stereotype.Component;

@Component
public class FitnessProfileMapper {

    public FitnessProfileResponse toResponse(
            FitnessProfile fitnessProfile
    ) {

        return FitnessProfileResponse.builder()
                .userId(fitnessProfile.getUserId())
                .height(fitnessProfile.getHeight())
                .weight(fitnessProfile.getWeight())
                .bmi(fitnessProfile.getBmi())
                .updatedAt(fitnessProfile.getUpdatedAt())
                .build();
    }
}
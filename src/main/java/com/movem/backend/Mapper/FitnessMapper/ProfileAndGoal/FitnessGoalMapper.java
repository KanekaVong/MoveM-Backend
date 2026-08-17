package com.movem.backend.Mapper.FitnessMapper.ProfileAndGoal;

import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessGoalResponse;
import com.movem.backend.Entity.Fitness.ProfileAndGoal.FitnessGoal;
import org.springframework.stereotype.Component;

@Component
public class FitnessGoalMapper {

    public FitnessGoalResponse toResponse(
            FitnessGoal fitnessGoal
    ) {

        return FitnessGoalResponse.builder()
                .id(fitnessGoal.getId())
                .userId(
                        fitnessGoal.getUser() != null
                        ? fitnessGoal.getUser().getId()
                                : null
                )

                .goalType(
                        fitnessGoal.getGoalType()
                )


                .targetWeight(
                        fitnessGoal.getTargetWeight()
                )

                .targetTimeline(
                        fitnessGoal.getTargetTimeline()
                )

                .workoutLevel(
                        fitnessGoal.getWorkoutLevel()
                )

                .estimatedWeightChange(
                        fitnessGoal.getEstimatedWeightChange()
                )

                .estimatedDailyDeficit(
                        fitnessGoal.getEstimatedDailyDeficit()
                )

                .status(
                        fitnessGoal.getStatus()
                )

                .createdAt(
                        fitnessGoal.getCreatedAt()
                )

                .updatedAt(
                        fitnessGoal.getUpdatedAt()
                )

                .build();
    }
}

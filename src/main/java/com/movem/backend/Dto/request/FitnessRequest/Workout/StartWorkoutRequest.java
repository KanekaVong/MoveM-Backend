package com.movem.backend.Dto.request.FitnessRequest.Workout;

import com.movem.backend.model.enums.Fitness.WorkoutType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartWorkoutRequest {

    private WorkoutType workoutType;

    private Integer soloChallengeId;

    private Integer participantId;
}
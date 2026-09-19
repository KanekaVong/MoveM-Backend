package com.movem.backend.Dto.request.FitnessRequest.Workout;

import com.movem.backend.model.enums.Fitness.WorkoutType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartWorkoutRequest {
     WorkoutType workoutType;
     Integer soloChallengeId;
     Integer participantId;
}
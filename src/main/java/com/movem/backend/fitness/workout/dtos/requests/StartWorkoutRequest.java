package com.movem.backend.fitness.workout.dtos.requests;

import com.movem.backend.commons.enums.Fitness.WorkoutType;
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
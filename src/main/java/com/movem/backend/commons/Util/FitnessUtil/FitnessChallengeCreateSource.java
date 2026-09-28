package com.movem.backend.commons.Util.FitnessUtil;

import com.movem.backend.commons.enums.Fitness.WorkoutType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessChallengeCreateSource implements FitnessCreateSource {
     String activityName;
     String description;
     LocalDateTime startActivity;
     LocalDateTime deadline;
     String parentActivityId;
     WorkoutType workoutType;
     Integer soloChallengeId;
     Integer participantId;
}

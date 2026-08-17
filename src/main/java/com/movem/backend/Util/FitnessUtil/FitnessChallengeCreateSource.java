package com.movem.backend.Util.FitnessUtil;

import com.movem.backend.model.enums.Fitness.WorkoutType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FitnessChallengeCreateSource implements FitnessCreateSource {

    private String activityName;

    private String description;

    private LocalDateTime startActivity;

    private LocalDateTime deadline;

    private String parentActivityId;

    private WorkoutType workoutType;
    private Integer soloChallengeId;
    private Integer participantId;
}

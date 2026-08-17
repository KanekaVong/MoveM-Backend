package com.movem.backend.Mapper.FitnessMapper.Workout;

import com.movem.backend.Dto.response.FitnessResponse.Workout.FitnessWorkoutSessionResponse;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import org.springframework.stereotype.Component;
import com.movem.backend.Dto.response.FitnessResponse.Workout.WorkoutHistoryResponse;

@Component
public class FitnessWorkoutSessionMapper {

    public FitnessWorkoutSessionResponse toResponse(
            FitnessWorkoutSession session
    ) {

        return FitnessWorkoutSessionResponse.builder()
                .sessionId(session.getId())

                .userId(
                        session.getUser() != null
                                ? session.getUser().getId()
                                : null
                )

                .soloChallengeId(
                        session.getSoloChallenge() != null
                                ? session.getSoloChallenge().getId()
                                : null
                )

                .groupChallengeParticipantId(
                        session.getGroupChallengeParticipant() != null
                                ? session.getGroupChallengeParticipant().getId()
                                : null
                )

                .workoutType(session.getWorkoutType())
                .status(session.getStatus())
                .startedAt(session.getStartedAt())
                .finishedAt(session.getFinishedAt())
                .durationSeconds(session.getDurationSeconds())
                .steps(session.getSteps())
                .distance(session.getDistance())
                .caloriesBurned(session.getCaloriesBurned())
                .averagePace(session.getAveragePace())

                .build();
    }

    public WorkoutHistoryResponse toHistoryResponse(
            FitnessWorkoutSession session
    ) {

        return WorkoutHistoryResponse.builder()
                .id(session.getId())
                .workoutType(session.getWorkoutType())
                .status(session.getStatus())
                .startedAt(session.getStartedAt())
                .finishedAt(session.getFinishedAt())
                .durationSeconds(session.getDurationSeconds())
                .distance(session.getDistance())
                .caloriesBurned(session.getCaloriesBurned())
                .build();
    }
}
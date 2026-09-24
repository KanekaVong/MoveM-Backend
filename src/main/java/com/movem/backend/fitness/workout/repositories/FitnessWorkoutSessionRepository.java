package com.movem.backend.fitness.workout.repositories;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.fitness.challenges.entities.FitnessChallengeParticipant;
import com.movem.backend.fitness.challenges.entities.SoloChallenge;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface FitnessWorkoutSessionRepository
        extends JpaRepository<FitnessWorkoutSession, Integer>,
        JpaSpecificationExecutor<FitnessWorkoutSession> {

    List<FitnessWorkoutSession> findByUser(
            User user
    );

    Optional<FitnessWorkoutSession> findByActivity(Activity activity);

    List<FitnessWorkoutSession> findBySoloChallenge(
            SoloChallenge soloChallenge
    );

    List<FitnessWorkoutSession> findByGroupChallengeParticipant(
            FitnessChallengeParticipant participant
    );

    Optional<FitnessWorkoutSession> findByIdAndUser(
            Integer sessionId,
            User user
    );

    List<FitnessWorkoutSession>
    findByUserAndStatusAndActivity_StatusNotOrderByFinishedAtDesc(
            User user,
            FitnessWorkoutStatus status,
            ActivityStatus activityStatus
    );



    List<FitnessWorkoutSession>
    findByUserAndActivity_StatusNot(
            User user,
            ActivityStatus status
    );

    Optional<FitnessWorkoutSession>
    findByIdAndUserAndActivity_StatusNot(
            Integer sessionId,
            User user,
            ActivityStatus status
    );

    List<FitnessWorkoutSession>
    findByUserAndStatusAndActivity_StatusNotAndFinishedAtBetweenOrderByFinishedAtDesc(
            User user,
            FitnessWorkoutStatus status,
            ActivityStatus activityStatus,
            java.time.LocalDateTime start,
            java.time.LocalDateTime end
    );

    List<FitnessWorkoutSession> findByUserAndStatus(
            User user,
            FitnessWorkoutStatus status
    );

    List<FitnessWorkoutSession>
    findByUserInAndStatusAndIsSharedTrueOrderByFinishedAtDesc(
            List<User> users,
            FitnessWorkoutStatus status
    );

}
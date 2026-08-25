package com.movem.backend.Repository.FitnessRepository.Workout;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Fitness.Challenge.FitnessChallengeParticipant;
import com.movem.backend.Entity.Fitness.Challenge.SoloChallenge;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FitnessWorkoutSessionRepository
        extends JpaRepository<FitnessWorkoutSession, Integer> {

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
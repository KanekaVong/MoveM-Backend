package com.movem.backend.social.like.repository;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.social.like.entity.Kudos;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KudosRepository
        extends JpaRepository<Kudos, Long> {

    boolean existsByWorkoutSessionAndUser(
            FitnessWorkoutSession workoutSession,
            User user
    );

    Optional<Kudos> findByWorkoutSessionAndUser(
            FitnessWorkoutSession workoutSession,
            User user
    );

    long countByWorkoutSession(
            FitnessWorkoutSession workoutSession
    );

    @Query("""
    SELECT k.workoutSession.id, COUNT(k)
    FROM Kudos k
    WHERE k.workoutSession.id IN :sessionIds
    GROUP BY k.workoutSession.id
""")
    List<Object[]> countKudosBySessionIds(
            @Param("sessionIds") List<Integer> sessionIds
    );

    @Query("""
    SELECT k.workoutSession.id
    FROM Kudos k
    WHERE k.workoutSession.id IN :sessionIds
      AND k.user.id = :userId
""")
    List<Integer> findSessionsWithMyKudos(
            @Param("sessionIds") List<Integer> sessionIds,
            @Param("userId") Integer userId
    );
}
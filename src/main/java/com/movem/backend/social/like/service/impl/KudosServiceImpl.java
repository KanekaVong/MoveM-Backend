package com.movem.backend.social.like.service.impl;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.social.like.entity.Kudos;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.social.comment.repository.CommentRepository;
import com.movem.backend.social.like.repository.KudosRepository;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutSessionRepository;
import com.movem.backend.social.friend.repositories.FriendRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.social.like.service.KudosService;
import com.movem.backend.commons.Event.Factory.Fitness.WorkoutSocialEventFactory;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.commons.Exception.UnauthorizedActionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class KudosServiceImpl
        implements KudosService {

    private final KudosRepository kudosRepository;
    private final CommentRepository commentRepository;    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final FriendRepository friendRepository;
    private final CurrentUserService currentUserService;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final WorkoutSocialEventFactory workoutSocialEventFactory;

    @Override
    public void giveKudos(Integer sessionId) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                getSession(sessionId);

        validateCanInteract(session, currentUser);

        if (kudosRepository
                .existsByWorkoutSessionAndUser(
                        session,
                        currentUser
                )) {

            throw new IllegalArgumentException(
                    "You have already given kudos to this workout."
            );
        }

        Kudos kudos =
                new Kudos();

        kudos.setWorkoutSession(session);
        kudos.setUser(currentUser);
        kudos.setCreatedAt(LocalDateTime.now());

        kudosRepository.save(kudos);

        featureEventTrackingService.handle(
                workoutSocialEventFactory.kudosGiven(
                        session,
                        currentUser
                )
        );
    }

    @Override
    public void removeKudos(Integer sessionId) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                getSession(sessionId);

        Kudos kudos =
                kudosRepository
                        .findByWorkoutSessionAndUser(
                                session,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Kudos not found."
                                )
                        );

        kudosRepository.delete(kudos);

        featureEventTrackingService.handle(
                workoutSocialEventFactory.kudosRemoved(
                        session,
                        currentUser
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public long getKudosCount(Integer sessionId) {

        FitnessWorkoutSession session =
                getSession(sessionId);

        return kudosRepository
                .countByWorkoutSession(session);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGivenKudos(Integer sessionId) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                getSession(sessionId);

        return kudosRepository
                .existsByWorkoutSessionAndUser(
                        session,
                        currentUser
                );
    }

    private FitnessWorkoutSession getSession(
            Integer sessionId
    ) {

        return workoutSessionRepository
                .findById(sessionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workout session not found."
                        )
                );
    }

    private void validateCanInteract(
            FitnessWorkoutSession session,
            User currentUser
    ) {

        if (session.getStatus() != FitnessWorkoutStatus.COMPLETED) {

            throw new IllegalArgumentException(
                    "Only completed workouts can receive kudos."
            );
        }

        if (session.getUser().getId().equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You cannot give kudos to your own workout."
            );
        }

        User owner = session.getUser();

        User first =
                owner.getId() < currentUser.getId()
                        ? owner
                        : currentUser;

        User second =
                owner.getId() < currentUser.getId()
                        ? currentUser
                        : owner;

        if (!friendRepository.existsByUserOneAndUserTwo(
                first,
                second
        )) {

            throw new UnauthorizedActionException(
                    "You can only give kudos to a friend's workout."
            );
        }
    }
}
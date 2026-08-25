package com.movem.backend.Service.Implement.SocialServices;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Social.Kudos;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.SocialRepository.CommentRepository;
import com.movem.backend.Repository.SocialRepository.KudosRepository;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutSessionRepository;
import com.movem.backend.Repository.FriendRepository.FriendRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.SocialServices.KudosService;
import com.movem.backend.Service.Event.Factory.Fitness.WorkoutSocialEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.Exception.UnauthorizedActionException;
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
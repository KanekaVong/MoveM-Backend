package com.movem.backend.social.comment.service.impl;

import com.movem.backend.social.comment.dtos.requests.CreateCommentRequest;
import com.movem.backend.social.comment.dtos.requests.UpdateCommentRequest;
import com.movem.backend.social.comment.dtos.response.CommentResponse;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.social.comment.entity.Comment;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.commons.Exception.UnauthorizedActionException;
import com.movem.backend.social.comment.mapper.CommentMapper;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutSessionRepository;
import com.movem.backend.social.friend.repositories.FriendRepository;
import com.movem.backend.shared.activity.repositories.ActivityRepository;
import com.movem.backend.social.comment.repository.CommentRepository;
import com.movem.backend.shared.activity.services.ActivityPermissionService;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.social.comment.service.CommentService;
import com.movem.backend.commons.Event.Factory.CommentEventFactory;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl
        implements CommentService {

    private final CommentRepository commentRepository;
    private final ActivityRepository activityRepository;
    private final CurrentUserService currentUserService;
    private final CommentEventFactory commentEventFactory;
    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final ActivityPermissionService activityPermissionService;
    private final FriendRepository friendRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(
            String activityId,
            CreateCommentRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Activity activity =
                activityRepository.findById(activityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Activity not found."
                                ));

        activityPermissionService.validateActivityAccess(
                activity,
                currentUser
        );

        Comment comment = new Comment();
        comment.setActivity(activity);
        comment.setUser(currentUser);
        comment.setContent(request.getContent());
        comment.setCreatedAt( LocalDateTime.now());
        Comment saved = commentRepository.save(comment);

        featureEventTrackingService.handle(
                commentEventFactory.created(saved, currentUser)
        );


        return commentMapper.toResponse(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(
            String activityId,
            Pageable pageable
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Activity activity =
                activityRepository.findById(activityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Activity not found."
                                ));

        activityPermissionService.validateActivityAccess(
                activity,
                currentUser
        );

        return commentRepository
                .findByActivityOrderByCreatedAtAsc(
                        activity,
                        pageable
                )
                .map(commentMapper::toResponse);

    }

    @Override
    public CommentResponse updateComment(
            Long commentId,
            UpdateCommentRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Comment comment =
                commentRepository
                        .findWithUserAndActivityById(commentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Comment not found."
                                ));

        if (!comment.getUser().getId().equals(currentUser.getId())) {

            throw new UnauthorizedActionException(
                    "You can only edit your own comments."
            );

        }

        String oldContent = comment.getContent();

        comment.setContent(request.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);

        featureEventTrackingService.handle(
                commentEventFactory.updated(
                        saved,
                        currentUser,
                        oldContent
                )
        );

        Activity activity = comment.getActivity();


        return commentMapper.toResponse(saved);

    }

    @Override
    public void deleteComment(
            Long commentId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Comment comment =
                commentRepository.findById(commentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Comment not found."
                                ));

        Activity activity = comment.getActivity();

        boolean isCommentOwner = comment.getUser().getId().equals(currentUser.getId());

        boolean isActivityOwner = activity.getUser().getId().equals(currentUser.getId());

        if (!isCommentOwner && !isActivityOwner) {

            throw new UnauthorizedActionException(
                    "You are not allowed to delete this comment."
            );

        }
        String oldContent = comment.getContent();

        commentRepository.delete(comment);

        featureEventTrackingService.handle(
                commentEventFactory.deleted(
                        comment,
                        currentUser
                )
        );


    }


    @Override
    public CommentResponse createWorkoutComment(
            Integer sessionId,
            CreateCommentRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (session.getStatus()
                != FitnessWorkoutStatus.COMPLETED) {

            throw new IllegalArgumentException(
                    "Only completed workouts can receive comments."
            );
        }

        if (!Boolean.TRUE.equals(
                session.getIsShared()
        )) {

            throw new UnauthorizedActionException(
                    "This workout has not been shared."
            );
        }

        User owner = session.getUser();

        boolean isOwner =
                owner.getId()
                        .equals(currentUser.getId());

        if (!isOwner) {

            User first =
                    owner.getId() < currentUser.getId()
                            ? owner
                            : currentUser;

            User second =
                    owner.getId() < currentUser.getId()
                            ? currentUser
                            : owner;

            if (!friendRepository
                    .existsByUserOneAndUserTwo(
                            first,
                            second
                    )) {

                throw new UnauthorizedActionException(
                        "You can only comment on a friend's shared workout."
                );
            }
        }

        Activity activity =
                session.getActivity();

        if (activity == null) {
            throw new ResourceNotFoundException(
                    "Workout activity not found."
            );
        }

        return createComment(
                activity.getId(),
                request
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getWorkoutComments(
            Integer sessionId,
            Pageable pageable
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (session.getStatus()
                != FitnessWorkoutStatus.COMPLETED) {

            throw new IllegalArgumentException(
                    "Only completed workouts can have comments."
            );
        }

        if (!Boolean.TRUE.equals(
                session.getIsShared()
        )) {

            throw new UnauthorizedActionException(
                    "This workout has not been shared."
            );
        }

        User owner = session.getUser();

        boolean isOwner =
                owner.getId()
                        .equals(currentUser.getId());

        if (!isOwner) {

            User first =
                    owner.getId() < currentUser.getId()
                            ? owner
                            : currentUser;

            User second =
                    owner.getId() < currentUser.getId()
                            ? currentUser
                            : owner;

            if (!friendRepository
                    .existsByUserOneAndUserTwo(
                            first,
                            second
                    )) {

                throw new UnauthorizedActionException(
                        "You can only view comments on a friend's shared workout."
                );
            }
        }

        Activity activity =
                session.getActivity();

        if (activity == null) {
            throw new ResourceNotFoundException(
                    "Workout activity not found."
            );
        }

        return commentRepository
                .findByActivityOrderByCreatedAtAsc(
                        activity,
                        pageable
                )
                .map(commentMapper::toResponse);
    }
}
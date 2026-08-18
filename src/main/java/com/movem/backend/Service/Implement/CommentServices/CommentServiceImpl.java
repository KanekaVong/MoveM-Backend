package com.movem.backend.Service.Implement.CommentServices;

import com.movem.backend.Dto.request.CommentRequest.CreateCommentRequest;
import com.movem.backend.Dto.request.CommentRequest.UpdateCommentRequest;
import com.movem.backend.Dto.response.CommentResponse.CommentResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Shared.Comment;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Exception.UnauthorizedActionException;
import com.movem.backend.Mapper.CommentMapper.CommentMapper;
import com.movem.backend.Service.SharedServices.Event.FeatureEventTrackingService;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Repository.CommentRepository.CommentRepository;
import com.movem.backend.Service.SharedServices.ActivityFeedService;
import com.movem.backend.Service.SharedServices.ActivityPermissionService;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.CommentServices.CommentService;
import com.movem.backend.Service.SharedServices.AuditLogService;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import com.movem.backend.model.enums.Notification.NotificationType;
import com.movem.backend.model.enums.Notification.ReferenceType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl
        implements CommentService {

    private final CommentRepository commentRepository;
    private final ActivityRepository activityRepository;
    private final CurrentUserService currentUserService;
    private final ActivityPermissionService activityPermissionService;
    private final ActivityFeedService activityFeedService;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final AuditLogService auditLogService;
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

        Comment comment =
                new Comment();

        comment.setActivity(activity);

        comment.setUser(currentUser);

        comment.setContent(
                request.getContent()
        );

        comment.setCreatedAt(
                LocalDateTime.now()
        );

        Comment saved =
                commentRepository.save(comment);

        featureEventTrackingService.handle(
                FeatureEvent.builder()

                        .activity(activity)
                        .actor(currentUser)
                        .feedEvent(ActivityFeedEvent.COMMENT_CREATED)
                        .auditCategory(AuditCategory.COMMENT)
                        .auditSeverity(AuditSeverity.INFO)
                        .auditEntity("comment")
                        .auditMessage("Created comment.")
                        .newValue(saved.getContent())
                        .notificationReceiver(
                                activity.getUser()
                                        .getId()
                                        .equals(currentUser.getId())
                                        ? null: activity.getUser())
                        .notificationType(NotificationType.COMMENT_CREATED)
                        .referenceType(ReferenceType.COMMENT)
                        .referenceId(String.valueOf(saved.getId())
                        )
                        .notificationTitle("New Comment")

                        .notificationMessage(
                                currentUser.getUsername()
                                        + " commented on your activity."
                        )

                        .actions(Set.of(
                                FeatureEventAction.ACTIVITY_FEED,
                                FeatureEventAction.AUDIT_LOG,
                                FeatureEventAction.NOTIFICATION))
                        .build());

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

        Activity activity = comment.getActivity();

        featureEventTrackingService.handle(
                FeatureEvent.builder()
                        .activity(activity)
                        .actor(currentUser)
                        .feedEvent(ActivityFeedEvent.COMMENT_UPDATED)
                        .auditCategory(AuditCategory.COMMENT)
                        .auditSeverity(AuditSeverity.INFO)
                        .auditEntity("content")
                        .auditMessage("Updated Comment.")
                        .referenceId(activity.getId())
                        .actions(
                                Set.of(
                                        FeatureEventAction.ACTIVITY_FEED,
                                        FeatureEventAction.AUDIT_LOG
                                )
                        )
                        .build()
        );

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

        Activity activity =
                comment.getActivity();

        boolean isCommentOwner =
                comment.getUser().getId().equals(currentUser.getId());

        boolean isActivityOwner =
                activity.getUser().getId().equals(currentUser.getId());

        if (!isCommentOwner && !isActivityOwner) {

            throw new UnauthorizedActionException(
                    "You are not allowed to delete this comment."
            );

        }
        String oldContent = comment.getContent();


        featureEventTrackingService.handle(
                FeatureEvent.builder()
                        .activity(activity)
                        .actor(currentUser)
                        .feedEvent(ActivityFeedEvent.COMMENT_DELETED)
                        .auditCategory(AuditCategory.COMMENT)
                        .auditSeverity(AuditSeverity.INFO)
                        .auditEntity("content")
                        .auditMessage("Deleted comment.")
                        .referenceId(activity.getId())
                        .actions(
                                Set.of(
                                        FeatureEventAction.ACTIVITY_FEED,
                                        FeatureEventAction.AUDIT_LOG
                                )
                        )
                        .build()
        );

        commentRepository.delete(comment);


    }

}
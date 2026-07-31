package com.movem.backend.service.Implement.CommentServices;

import com.movem.backend.dto.request.CommentRequest.CreateCommentRequest;
import com.movem.backend.dto.request.CommentRequest.UpdateCommentRequest;
import com.movem.backend.dto.response.CommentResponse.CommentResponse;
import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Comment;
import com.movem.backend.entity.User;
import com.movem.backend.exception.ResourceNotFoundException;
import com.movem.backend.exception.UnauthorizedActionException;
import com.movem.backend.mapper.CommentMapper.CommentMapper;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.repository.SharedRepository.ActivityRepository;
import com.movem.backend.repository.CommentRepository.CommentRepository;
import com.movem.backend.service.SharedServices.ActivityFeedService;
import com.movem.backend.service.SharedServices.ActivityPermissionService;
import com.movem.backend.service.AuthServices.CurrentUserService;
import com.movem.backend.service.CommentServices.CommentService;
import com.movem.backend.service.SharedServices.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final ActivityPermissionService activityPermissionService;

    private final ActivityFeedService activityFeedService;

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

        activityFeedService.createFeed(
                activity,
                currentUser,
                ActivityFeedEvent.COMMENT_CREATED,
                "Commented.",
                saved.getId()
        );

        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.COMMENT_CREATED,
                AuditCategory.COMMENT,
                AuditSeverity.INFO,
                "comment",
                "Created comment.",
                null,
                saved.getContent()
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

        Activity activity = comment.getActivity();

        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.COMMENT_UPDATED,
                AuditCategory.COMMENT,
                AuditSeverity.INFO,
                "content",
                "Updated comment.",
                oldContent,
                saved.getContent()
        );

        activityFeedService.createFeed(
                activity,
                currentUser,
                ActivityFeedEvent.COMMENT_UPDATED,
                "Updated a comment.",
                saved.getId()
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


        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.COMMENT_DELETED,
                AuditCategory.COMMENT,
                AuditSeverity.WARNING,
                "comment",
                "Deleted comment.",
                oldContent,
                null
        );

        activityFeedService.createFeed(
                activity,
                currentUser,
                ActivityFeedEvent.COMMENT_DELETED,
                "Deleted a comment.",
                comment.getId()
        );

        commentRepository.delete(comment);


    }

}
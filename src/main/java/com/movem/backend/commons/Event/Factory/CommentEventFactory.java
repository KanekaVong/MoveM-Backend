package com.movem.backend.commons.Event.Factory;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.social.comment.entity.Comment;
import com.movem.backend.commons.Event.FeatureEvent;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.commons.enums.HistoryandLogs.AuditSeverity;
import com.movem.backend.commons.enums.HistoryandLogs.FeatureEventAction;
import com.movem.backend.commons.enums.Notification.NotificationType;
import com.movem.backend.commons.enums.Notification.ReferenceType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CommentEventFactory {

    public FeatureEvent created(
            Comment comment,
            User actor
    ) {

        Activity activity = comment.getActivity();

        User receiver =
                activity.getUser().getId().equals(actor.getId())
                        ? null
                        : activity.getUser();

        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.COMMENT_CREATED)
                .feedMessage("commented on the activity.")
                .auditCategory(AuditCategory.COMMENT)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("comment")
                .auditMessage("Created comment.")
                .newValue(comment.getContent())
                .notificationReceiver(receiver)
                .notificationType(NotificationType.COMMENT_CREATED)
                .referenceType(ReferenceType.COMMENT)
                .referenceId(String.valueOf(comment.getId()))
                .notificationTitle("New Comment")
                .notificationMessage(
                        actor.getUsername()
                                + " commented on your activity."
                )
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent updated(
            Comment comment,
            User actor,
            String oldContent
    ) {

        return FeatureEvent.builder()
                .activity(comment.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.COMMENT_UPDATED)
                .feedMessage("updated a comment.")
                .auditCategory(AuditCategory.COMMENT)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("content")
                .auditMessage("Updated comment.")
                .oldValue(oldContent)
                .newValue(comment.getContent())
                .referenceId(String.valueOf(comment.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent deleted(
            Comment comment,
            User actor
    ) {

        return FeatureEvent.builder()
                .activity(comment.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.COMMENT_DELETED)
                .feedMessage("deleted a comment.")
                .auditCategory(AuditCategory.COMMENT)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("content")
                .auditMessage("Deleted comment.")
                .oldValue(comment.getContent())
                .referenceId(String.valueOf(comment.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }
}
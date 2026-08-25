package com.movem.backend.Service.Event.Factory;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
@Component
public class TaskEventFactory {

    public FeatureEvent taskCreated(Activity activity, User actor) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TASK_CREATED)
                .feedMessage("created the task.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditMessage("Created task.")
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent taskDeleted(Activity activity, User actor) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TASK_DELETED)
                .feedMessage("deleted the task.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("status")
                .auditMessage("Moved task to recycle bin.")
                .oldValue("ACTIVE")
                .newValue("DELETED")
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent taskRestored(Activity activity, User actor) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TASK_RESTORED)
                .feedMessage("restored the task.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Restored task.")
                .oldValue("DELETED")
                .newValue("PENDING")
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent taskCompleted(Activity activity, User actor) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TASK_COMPLETED)
                .feedMessage("completed the task.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Completed task.")
                .oldValue(ActivityStatus.PENDING.name())
                .newValue(ActivityStatus.COMPLETE.name())
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent deadlineChanged(
            Activity activity,
            User actor,
            LocalDateTime oldDeadline,
            LocalDateTime newDeadline
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.DEADLINE_CHANGED)
                .feedMessage("changed the deadline.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("deadline")
                .auditMessage("Changed deadline.")
                .oldValue(oldDeadline == null ? null : oldDeadline.toString())
                .newValue(newDeadline == null ? null : newDeadline.toString())
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent taskUpdated(
            Activity activity,
            User actor,
            String entity,
            String auditMessage,
            String oldValue,
            String newValue,
            boolean includeFeed
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TASK_UPDATED)
                .feedMessage("updated the task.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity(entity)
                .auditMessage(auditMessage)
                .oldValue(oldValue)
                .newValue(newValue)
                .actions(
                        includeFeed
                                ? Set.of(
                                FeatureEventAction.ACTIVITY_FEED,
                                FeatureEventAction.AUDIT_LOG
                        )
                                : Set.of(
                                FeatureEventAction.AUDIT_LOG
                        )
                )
                .build();
    }

    public FeatureEvent taskRecurred(
            Activity activity,
            User actor
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TASK_RECURRED)
                .feedMessage("generated the next recurring task.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditMessage("Generated next recurring task.")
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }


    public FeatureEvent labelAdded(
            User actor,
            String labelName
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.LABEL_ADDED)
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("label")
                .auditMessage("Created label.")
                .newValue(labelName)
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent labelUpdated(
            User actor,
            String oldName,
            String newName
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.LABEL_ADDED)
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("label")
                .auditMessage("Updated label.")
                .oldValue(oldName)
                .newValue(newName)
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent labelRemoved(
            User actor,
            String oldName
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.LABEL_REMOVED)
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("label")
                .auditMessage("Deleted label.")
                .oldValue(oldName)
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }
}

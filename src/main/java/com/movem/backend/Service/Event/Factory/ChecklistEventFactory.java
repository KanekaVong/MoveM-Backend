package com.movem.backend.Service.Event.Factory;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Tasks.TaskChecklist;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ChecklistEventFactory {

    public FeatureEvent added(Activity activity, User actor, int count) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_ADDED)
                .feedMessage(count == 1
                        ? "added a checklist item."
                        : "added checklist items.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage(count == 1
                        ? "Added checklist item."
                        : "Added checklist items.")
                .newValue(String.valueOf(count))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent completed(TaskChecklist checklist, User actor) {
        return FeatureEvent.builder()
                .activity(checklist.getTask().getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_COMPLETED)
                .feedMessage("completed a checklist item.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage("Completed checklist item.")
                .oldValue("INCOMPLETE")
                .newValue("COMPLETED")
                .referenceId(String.valueOf(checklist.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent updated(
            TaskChecklist checklist,
            User actor,
            String oldName
    ) {
        return FeatureEvent.builder()
                .activity(checklist.getTask().getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_UPDATED)
                .feedMessage("updated a checklist item.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage("Updated checklist item.")
                .oldValue(oldName)
                .newValue(checklist.getItemName())
                .referenceId(String.valueOf(checklist.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent removed(TaskChecklist checklist, User actor) {
        return FeatureEvent.builder()
                .activity(checklist.getTask().getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_REMOVED)
                .feedMessage("deleted a checklist item.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage("Deleted checklist item.")
                .oldValue(checklist.getItemName())
                .referenceId(String.valueOf(checklist.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent toggled(
            TaskChecklist checklist,
            User actor,
            boolean oldCompleted,
            boolean newCompleted
    ) {
        return FeatureEvent.builder()
                .activity(checklist.getTask().getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_COMPLETED)
                .feedMessage(newCompleted
                        ? "completed a checklist item."
                        : "marked a checklist item as incomplete.")
                .auditCategory(AuditCategory.TASK)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage(newCompleted
                        ? "Completed checklist item."
                        : "Marked checklist item as incomplete.")
                .oldValue(oldCompleted ? "COMPLETED" : "INCOMPLETE")
                .newValue(newCompleted ? "COMPLETED" : "INCOMPLETE")
                .referenceId(String.valueOf(checklist.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }
}
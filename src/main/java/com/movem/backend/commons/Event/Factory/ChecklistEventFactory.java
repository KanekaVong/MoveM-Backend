package com.movem.backend.commons.Event.Factory;

import com.movem.backend.commons.enums.shared.ActivityType;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.shared.checklist.entities.Checklist;
import com.movem.backend.commons.Event.FeatureEvent;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.commons.enums.HistoryandLogs.AuditSeverity;
import com.movem.backend.commons.enums.HistoryandLogs.FeatureEventAction;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ChecklistEventFactory {

    public FeatureEvent added(Activity activity, User actor, int count) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_ADDED)
                .feedMessage(count == 1 ? "added a checklist item." : "added checklist items.")
                .auditCategory(getAuditCategory(activity))
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage(count == 1 ? "Added checklist item." : "Added checklist items.")
                .newValue(String.valueOf(count))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent completed(Checklist checklist, User actor) {
        Activity activity = getActivity(checklist);

        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_COMPLETED)
                .feedMessage("completed a checklist item.")
                .auditCategory(getAuditCategory(checklist))
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage("Completed checklist item.")
                .oldValue("INCOMPLETE")
                .newValue("COMPLETED")
                .referenceId(String.valueOf(checklist.getId()))
                .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                .build();
    }

    public FeatureEvent updated(Checklist checklist, User actor, String oldName) {
        Activity activity = getActivity(checklist);

        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_UPDATED)
                .feedMessage("updated a checklist item.")
                .auditCategory(getAuditCategory(checklist))
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage("Updated checklist item.")
                .oldValue(oldName)
                .newValue(checklist.getItemName())
                .referenceId(String.valueOf(checklist.getId()))
                .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                .build();
    }

    public FeatureEvent removed(Checklist checklist, User actor) {
        Activity activity = getActivity(checklist);

        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_REMOVED)
                .feedMessage("deleted a checklist item.")
                .auditCategory(getAuditCategory(checklist))
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage("Deleted checklist item.")
                .oldValue(checklist.getItemName())
                .referenceId(String.valueOf(checklist.getId()))
                .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                .build();
    }

    public FeatureEvent toggled(Checklist checklist, User actor, boolean oldCompleted, boolean newCompleted) {
        Activity activity = getActivity(checklist);

        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHECKLIST_COMPLETED)
                .feedMessage(newCompleted ? "completed a checklist item." : "marked a checklist item as incomplete.")
                .auditCategory(getAuditCategory(checklist))
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("checklist")
                .auditMessage(newCompleted ? "Completed checklist item." : "Marked checklist item as incomplete.")
                .oldValue(oldCompleted ? "COMPLETED" : "INCOMPLETE")
                .newValue(newCompleted ? "COMPLETED" : "INCOMPLETE")
                .referenceId(String.valueOf(checklist.getId()))
                .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                .build();
    }

    private Activity getActivity(Checklist checklist) {
        if (checklist.getTask() != null) {
            return checklist.getTask().getActivity();
        }

        if (checklist.getTrip() != null) {
            return checklist.getTrip().getActivity();
        }
        throw new IllegalStateException("Checklist must belong to either a Task or Trip.");
    }

    private AuditCategory getAuditCategory(Checklist checklist) {
        if (checklist.getTask() != null) {
            return AuditCategory.TASK;
        }

        if (checklist.getTrip() != null) {
            return AuditCategory.TRIP;
        }
        throw new IllegalStateException("Checklist must belong to either a Task or Trip.");
    }
    private AuditCategory getAuditCategory(Activity activity) {
        return activity.getActivityType() == ActivityType.TRIP ? AuditCategory.TRIP : AuditCategory.TASK;
    }
}
package com.movem.backend.commons.Event.Factory.Fitness;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
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
public class WorkoutSocialEventFactory {

    public FeatureEvent kudosGiven(
            FitnessWorkoutSession session,
            User actor
    ) {
        User owner = session.getUser();

        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.WORKOUT_KUDOED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("workout_kudos")
                .auditMessage("Gave kudos to a workout.")
                .newValue(owner.getUsername())
                .referenceId(String.valueOf(session.getId()))
                .notificationReceiver(owner)
                .notificationType(NotificationType.WORKOUT_KUDOED)
                .referenceType(ReferenceType.FITNESS)
                .notificationTitle("New Kudos")
                .notificationMessage(
                        actor.getUsername() +
                                " gave kudos to your workout."
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent kudosRemoved(
            FitnessWorkoutSession session,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.WORKOUT_KUDO_REMOVED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("workout_kudos")
                .auditMessage("Removed workout kudos.")
                .referenceId(String.valueOf(session.getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }
}
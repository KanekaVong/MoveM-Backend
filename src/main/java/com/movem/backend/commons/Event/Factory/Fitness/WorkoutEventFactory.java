package com.movem.backend.commons.Event.Factory.Fitness;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.commons.Event.FeatureEvent;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.commons.enums.HistoryandLogs.AuditSeverity;
import com.movem.backend.commons.enums.HistoryandLogs.FeatureEventAction;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class WorkoutEventFactory {

    public FeatureEvent completed(
            FitnessWorkoutSession session,
            User actor
    ) {
        return FeatureEvent.builder()
                .activity(session.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.WORKOUT_COMPLETED)
                .feedMessage("completed a workout.")
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("workout")
                .auditMessage("Completed workout.")
                .newValue(session.getWorkoutType().name())
                .referenceId(String.valueOf(session.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }
}
package com.movem.backend.Service.Event.Factory.Fitness;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Event.FeatureEventAction;
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
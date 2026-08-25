package com.movem.backend.Service.Event.Factory.Fitness;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.Challenge.GroupChallengeCatalog;
import com.movem.backend.Entity.Fitness.Challenge.GroupFitnessChallenge;
import com.movem.backend.Entity.Fitness.Challenge.SoloChallenge;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import com.movem.backend.model.enums.Fitness.FitnessChallengeStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FitnessChallengeEventFactory {

    public FeatureEvent created(GroupFitnessChallenge challenge, User actor) {
        return FeatureEvent.builder()
                .activity(challenge.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_CREATED)
                .feedMessage("created a fitness challenge.")
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("challenge")
                .auditMessage("Created fitness challenge.")
                .newValue(challenge.getName())
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                .build();
    }

    public FeatureEvent updated(GroupFitnessChallenge challenge, User actor) {
        return FeatureEvent.builder()
                .activity(challenge.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_UPDATED)
                .feedMessage("updated the fitness challenge.")
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("challenge")
                .auditMessage("Updated fitness challenge.")
                .newValue(challenge.getName())
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                .build();
    }

    public FeatureEvent cancelled(GroupFitnessChallenge challenge, User actor) {
        return FeatureEvent.builder()
                .activity(challenge.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_CANCELLED)
                .feedMessage("cancelled the fitness challenge.")
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("status")
                .auditMessage("Cancelled fitness challenge.")
                .oldValue("ACTIVE")
                .newValue("CANCELLED")
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                .build();
    }
    public FeatureEvent joined(
            GroupFitnessChallenge challenge,
            User actor
    ) {
        return FeatureEvent.builder()
                .activity(challenge.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_JOINED)
                .feedMessage("joined the challenge.")
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("challenge_participant")
                .auditMessage("Joined fitness challenge.")
                .newValue(actor.getUsername())
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent left(
            GroupFitnessChallenge challenge,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_LEFT)
                .feedMessage("left the fitness challenge.")
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("participant")
                .auditMessage("Left fitness challenge.")
                .oldValue("ACTIVE")
                .newValue("LEFT")
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent challengeStarted(
            GroupFitnessChallenge challenge
    ) {
        return FeatureEvent.builder()
                .activity(challenge.getActivity())
                .actor(challenge.getActivity().getUser())
                .feedEvent(ActivityFeedEvent.CHALLENGE_STARTED)
                .feedMessage("started the fitness challenge.")
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Fitness challenge started.")
                .oldValue(FitnessChallengeStatus.UPCOMING.name())
                .newValue(FitnessChallengeStatus.IN_PROGRESS.name())
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent challengeCompleted(
            GroupFitnessChallenge challenge
    ) {
        return FeatureEvent.builder()
                .activity(challenge.getActivity())
                .actor(challenge.getActivity().getUser())
                .feedEvent(ActivityFeedEvent.CHALLENGE_COMPLETED)
                .feedMessage("completed the fitness challenge.")
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Fitness challenge completed.")
                .oldValue(FitnessChallengeStatus.IN_PROGRESS.name())
                .newValue(FitnessChallengeStatus.COMPLETE.name())
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent created(GroupChallengeCatalog catalog, User actor) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_CREATED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("challenge_catalog")
                .auditMessage("Created fitness challenge catalog.")
                .newValue(catalog.getName())
                .referenceId(String.valueOf(catalog.getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent updated(
            GroupChallengeCatalog catalog,
            User actor,
            String oldName
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_UPDATED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("challenge_catalog")
                .auditMessage("Updated fitness challenge catalog.")
                .oldValue(oldName)
                .newValue(catalog.getName())
                .referenceId(String.valueOf(catalog.getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent deleted(
            GroupChallengeCatalog catalog,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_REMOVED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("challenge_catalog")
                .auditMessage("Deleted fitness challenge catalog.")
                .oldValue(catalog.getName())
                .referenceId(String.valueOf(catalog.getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent soloChallengeCreated(SoloChallenge challenge, User actor) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_CREATED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("solo_challenge")
                .auditMessage("Created solo fitness challenge.")
                .newValue(challenge.getName())
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(FeatureEventAction.AUDIT_LOG))
                .build();
    }

    public FeatureEvent soloChallengeUpdated(
            SoloChallenge challenge,
            User actor,
            String oldName
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_UPDATED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("solo_challenge")
                .auditMessage("Updated solo fitness challenge.")
                .oldValue(oldName)
                .newValue(challenge.getName())
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(FeatureEventAction.AUDIT_LOG))
                .build();
    }

    public FeatureEvent soloChallengeDeleted(
            SoloChallenge challenge,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.CHALLENGE_REMOVED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("solo_challenge")
                .auditMessage("Deleted solo fitness challenge.")
                .oldValue(challenge.getName())
                .referenceId(String.valueOf(challenge.getId()))
                .actions(Set.of(FeatureEventAction.AUDIT_LOG))
                .build();
    }
}
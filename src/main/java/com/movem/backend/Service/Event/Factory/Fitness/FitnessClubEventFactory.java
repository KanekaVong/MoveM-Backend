package com.movem.backend.Service.Event.Factory.Fitness;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Fitness.Club.FitnessClubJoinRequest;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMember;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import com.movem.backend.model.enums.Notification.NotificationType;
import com.movem.backend.model.enums.Notification.ReferenceType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FitnessClubEventFactory {

    public FeatureEvent clubCreated(
            FitnessClub club,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.GROUP_CREATED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("fitness_club")
                .auditMessage("Created fitness club.")
                .newValue(club.getName())
                .referenceId(String.valueOf(club.getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent clubUpdated(
            FitnessClub club,
            User actor,
            String oldName
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.GROUP_UPDATED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("fitness_club")
                .auditMessage("Updated fitness club.")
                .oldValue(oldName)
                .newValue(club.getName())
                .referenceId(String.valueOf(club.getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent clubDeleted(
            FitnessClub club,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.GROUP_DELETED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("fitness_club")
                .auditMessage("Deleted fitness club.")
                .oldValue(club.getName())
                .referenceId(String.valueOf(club.getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent memberAdded(
            FitnessClub club,
            User actor,
            FitnessClubMember member
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.MEMBER_JOINED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("club_member")
                .auditMessage("Added member to fitness club.")
                .newValue(member.getUser().getUsername())
                .referenceId(String.valueOf(member.getUser().getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent memberJoined(
            FitnessClub club,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.MEMBER_JOINED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("club_member")
                .auditMessage("Joined fitness club.")
                .newValue(actor.getUsername())
                .referenceId(String.valueOf(club.getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent memberRoleUpdated(
            FitnessClub club,
            User actor,
            FitnessClubMember member,
            String oldRole
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.MEMBER_UPDATED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("club_member_role")
                .auditMessage("Updated club member role.")
                .oldValue(oldRole)
                .newValue(member.getRole().name())
                .referenceId(String.valueOf(member.getUser().getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent memberRemoved(
            FitnessClub club,
            User actor,
            FitnessClubMember member
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.MEMBER_REMOVED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("club_member")
                .auditMessage("Removed member from fitness club.")
                .oldValue(member.getUser().getUsername())
                .referenceId(String.valueOf(member.getUser().getId()))
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent joinRequestSent(
            FitnessClub club,
            FitnessClubJoinRequest request
    ) {
        return FeatureEvent.builder()
                .actor(request.getRequester())
                .feedEvent(ActivityFeedEvent.JOIN_REQUEST_SENT)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("club_join_request")
                .auditMessage("Requested to join fitness club.")
                .newValue("PENDING")
                .referenceId(String.valueOf(request.getId()))
                .notificationReceiver(club.getCreatedBy())
                .notificationType(NotificationType.GROUP_JOIN_REQUEST)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Fitness Club Join Request")
                .notificationMessage(
                        request.getRequester().getUsername()
                                + " requested to join "
                                + club.getName()
                                + "."
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent joinRequestApproved(
            FitnessClub club,
            FitnessClubJoinRequest request,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.JOIN_REQUEST_APPROVED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("club_join_request")
                .auditMessage("Approved fitness club join request.")
                .oldValue("PENDING")
                .newValue("APPROVED")
                .referenceId(String.valueOf(request.getId()))
                .notificationReceiver(request.getRequester())
                .notificationType(NotificationType.GROUP_JOIN_REQUEST_APPROVED)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Join Request Approved")
                .notificationMessage(
                        "Your request to join "
                                + club.getName()
                                + " was approved."
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent joinRequestRejected(
            FitnessClub club,
            FitnessClubJoinRequest request,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(ActivityFeedEvent.JOIN_REQUEST_REJECTED)
                .auditCategory(AuditCategory.FITNESS)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("club_join_request")
                .auditMessage("Rejected fitness club join request.")
                .oldValue("PENDING")
                .newValue("REJECTED")
                .referenceId(String.valueOf(request.getId()))
                .notificationReceiver(request.getRequester())
                .notificationType(NotificationType.GROUP_JOIN_REQUEST_REJECTED)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Join Request Rejected")
                .notificationMessage(
                        "Your request to join "
                                + club.getName()
                                + " was rejected."
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }
}
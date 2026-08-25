package com.movem.backend.Service.Event.Factory;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Collaboration.ActivityGroup;
import com.movem.backend.Entity.Shared.JoinRequest;
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
public class GroupEventFactory {

    public FeatureEvent groupCreated(
            Activity activity,
            User actor
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.GROUP_CREATED)
                .feedMessage("created the group.")
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.ACTIVITY_FEED
                ))
                .build();
    }

    public FeatureEvent memberInvited(
            Activity activity,
            User actor,
            User invitee,
            Long inviteId
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.MEMBER_INVITED)
                .feedMessage("invited " + invitee.getUsername() + ".")
                .auditCategory(AuditCategory.GROUP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("member")
                .auditMessage("Invited member.")
                .newValue(invitee.getUsername())
                .referenceId(activity.getId())                .notificationReceiver(invitee)
                .notificationType(NotificationType.GROUP_INVITE)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Group Invitation")
                .notificationMessage(
                        actor.getUsername()
                                + " invited you to join "
                                + activity.getActivityName()
                                + "."
                )
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent memberJoined(
            Activity activity,
            User actor
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.MEMBER_JOINED)
                .feedMessage("joined the group.")
                .auditCategory(AuditCategory.GROUP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("member")
                .auditMessage("Joined group.")
                .newValue(actor.getUsername())
                .notificationReceiver(null)
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent inviteAccepted(
            Activity activity,
            User actor,
            Long inviteId,
            User inviter
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.INVITE_ACCEPTED)
                .feedMessage(
                        actor.getUsername()
                                + " accepted the invitation."
                )
                .auditCategory(AuditCategory.GROUP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Accepted invitation.")
                .oldValue("PENDING")
                .newValue("ACCEPTED")
                .referenceId(activity.getId())

                .notificationReceiver(inviter)
                .notificationType(NotificationType.GROUP_INVITE_ACCEPTED)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Invitation Accepted")
                .notificationMessage(
                        actor.getUsername()
                                + " accepted your invitation to "
                                + activity.getActivityName()
                                + "."
                )

                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent inviteRejected(
            Activity activity,
            User actor,
            Long inviteId,
            User inviter
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.INVITE_REJECTED)
                .feedMessage(
                        actor.getUsername()
                                + " rejected the invitation."
                )
                .auditCategory(AuditCategory.GROUP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Rejected invitation.")
                .oldValue("PENDING")
                .newValue("REJECTED")
                .referenceId(activity.getId())

                .notificationReceiver(inviter)
                .notificationType(NotificationType.GROUP_INVITE_REJECTED)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Invitation Rejected")
                .notificationMessage(
                        actor.getUsername()
                                + " rejected your invitation to "
                                + activity.getActivityName()
                                + "."
                )

                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent joinRequestSent(
            Activity activity,
            User actor,
            ActivityGroup group,
            Long requestId
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.JOIN_REQUEST_SENT)
                .feedMessage("requested to join the activity.")
                .auditCategory(AuditCategory.GROUP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Requested to join.")
                .newValue("PENDING")
                .referenceId(activity.getId())                .notificationReceiver(group.getCreatedBy())
                .notificationType(NotificationType.GROUP_JOIN_REQUEST)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Group Join Request")
                .notificationMessage(
                        actor.getUsername()
                                + " requested to join "
                                + activity.getActivityName()
                                + "."
                )
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent joinRequestApproved(
            Activity activity,
            User actor,
            JoinRequest joinRequest
    ) {
        User requester = joinRequest.getRequester();

        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.JOIN_REQUEST_APPROVED)
                .feedMessage("joined the activity.")
                .auditCategory(AuditCategory.GROUP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Approved join request.")
                .oldValue("PENDING")
                .newValue("APPROVED")
                .referenceId(activity.getId())
                .notificationReceiver(requester)
                .notificationType(NotificationType.GROUP_JOIN_REQUEST_APPROVED)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Join Request Approved")
                .notificationMessage(
                        "Your request to join "
                                + activity.getActivityName()
                                + " was approved."
                )
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent joinRequestRejected(
            Activity activity,
            User actor,
            JoinRequest joinRequest
    ) {
        User requester = joinRequest.getRequester();

        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.JOIN_REQUEST_REJECTED)
                .feedMessage("rejected a join request.")
                .auditCategory(AuditCategory.GROUP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("member")
                .auditMessage("Rejected join request.")
                .oldValue(null)
                .newValue(requester.getUsername())
                .referenceId(activity.getId())                .notificationReceiver(requester)
                .notificationType(NotificationType.GROUP_JOIN_REQUEST_REJECTED)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Join Request Rejected")
                .notificationMessage(
                        "Your request to join "
                                + activity.getActivityName()
                                + " was rejected."
                )
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent memberLeft(
            Activity activity,
            User actor,
            ActivityGroup group
    ) {
        User groupOwner = group.getCreatedBy();

        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.MEMBER_LEFT)
                .feedMessage("left the activity.")
                .auditCategory(AuditCategory.GROUP)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("member")
                .auditMessage("Left group.")
                .oldValue(actor.getUsername())
                .newValue(null)
                .referenceId(null)
                .notificationReceiver(groupOwner)
                .notificationType(NotificationType.GROUP_MEMBER_LEFT)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Member Left Group")
                .notificationMessage(
                        actor.getUsername()
                                + " left "
                                + activity.getActivityName()
                                + "."
                )
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent memberRemoved(
            Activity activity,
            User actor,
            User member
    ) {
        return FeatureEvent.builder()
                .activity(activity)
                .actor(actor)
                .feedEvent(ActivityFeedEvent.MEMBER_REMOVED)
                .feedMessage(
                        "removed " + member.getUsername() + "."
                )
                .auditCategory(AuditCategory.GROUP)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("member")
                .auditMessage("Removed member.")
                .oldValue(member.getUsername())
                .newValue(null)
                .referenceId(null)
                .notificationReceiver(member)
                .notificationType(NotificationType.GROUP_MEMBER_REMOVED)
                .referenceType(ReferenceType.GROUP)
                .notificationTitle("Removed From Group")
                .notificationMessage(
                        "You were removed from "
                                + activity.getActivityName()
                                + "."
                )
                .actions(Set.of(
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }
}

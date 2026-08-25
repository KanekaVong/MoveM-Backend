package com.movem.backend.Service.Event.Factory;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Friend.FriendRequest;
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
public class FriendEventFactory {

    public FeatureEvent friendRequestSent(
            User sender,
            User receiver
    ) {
        return FeatureEvent.builder()
                .actor(sender)
                .feedEvent(ActivityFeedEvent.FRIEND_REQUEST_SENT)
                .auditCategory(AuditCategory.FRIEND)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("friend_request")
                .auditMessage("Sent friend request.")
                .newValue(receiver.getUsername())
                .notificationReceiver(receiver)
                .notificationType(NotificationType.FRIEND_REQUEST)
                .referenceType(ReferenceType.USER)
                .referenceId(sender.getId().toString())
                .notificationTitle("New Friend Request")
                .notificationMessage(
                        sender.getUsername()
                                + " sent you a friend request."
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent friendRequestAccepted(
            FriendRequest request,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(
                        ActivityFeedEvent.FRIEND_REQUEST_ACCEPTED
                )
                .auditCategory(AuditCategory.FRIEND)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Accepted friend request.")
                .newValue(
                        request.getSender().getUsername()
                )
                .notificationReceiver(
                        request.getSender()
                )
                .notificationType(
                        NotificationType.FRIEND_ACCEPTED
                )
                .referenceType(ReferenceType.USER)
                .referenceId(
                        actor.getId().toString()
                )
                .notificationTitle(
                        "Friend Request Accepted"
                )
                .notificationMessage(
                        actor.getUsername()
                                + " accepted your friend request."
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.NOTIFICATION
                ))
                .build();
    }

    public FeatureEvent friendRequestRejected(
            FriendRequest request,
            User actor
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(
                        ActivityFeedEvent.FRIEND_REQUEST_REJECTED
                )
                .auditCategory(AuditCategory.FRIEND)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("status")
                .auditMessage("Rejected friend request.")
                .newValue(
                        request.getSender().getUsername()
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }

    public FeatureEvent friendRemoved(
            User actor,
            User friend
    ) {
        return FeatureEvent.builder()
                .actor(actor)
                .feedEvent(
                        ActivityFeedEvent.FRIEND_REMOVED
                )
                .auditCategory(AuditCategory.FRIEND)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("friend")
                .auditMessage("Removed friend.")
                .oldValue(friend.getUsername())
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG
                ))
                .build();
    }
}
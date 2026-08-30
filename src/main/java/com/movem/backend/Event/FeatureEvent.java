package com.movem.backend.Event;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import com.movem.backend.model.enums.Notification.NotificationType;
import com.movem.backend.model.enums.Notification.ReferenceType;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class FeatureEvent {

    private Activity activity;
    private User actor;

    private ActivityFeedEvent feedEvent;
    private String feedMessage;


    private AuditCategory auditCategory;
    private AuditSeverity auditSeverity;

    private String auditEntity;
    private String auditMessage;

    private String oldValue;
    private String newValue;

    private User notificationReceiver;

    private boolean notifyActivityGroup;

    private NotificationType notificationType;
    private ReferenceType referenceType;

    private String referenceId;
    private String feedReferenceId;

    private String notificationTitle;
    private String notificationMessage;

    private Set<FeatureEventAction> actions;
}
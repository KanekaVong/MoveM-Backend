package com.movem.backend.commons.Event;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.commons.enums.HistoryandLogs.AuditSeverity;
import com.movem.backend.commons.enums.HistoryandLogs.FeatureEventAction;
import com.movem.backend.commons.enums.Notification.NotificationType;
import com.movem.backend.commons.enums.Notification.ReferenceType;
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
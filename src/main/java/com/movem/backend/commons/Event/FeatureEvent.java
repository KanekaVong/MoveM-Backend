package com.movem.backend.commons.Event;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.commons.enums.HistoryandLogs.AuditSeverity;
import com.movem.backend.commons.enums.HistoryandLogs.FeatureEventAction;
import com.movem.backend.commons.enums.Notification.NotificationType;
import com.movem.backend.commons.enums.Notification.ReferenceType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeatureEvent {
     Activity activity;
     User actor;

     ActivityFeedEvent feedEvent;
     String feedMessage;


     AuditCategory auditCategory;
     AuditSeverity auditSeverity;

     String auditEntity;
     String auditMessage;

     String oldValue;
     String newValue;

     User notificationReceiver;

     boolean notifyActivityGroup;

     NotificationType notificationType;
     ReferenceType referenceType;

     String referenceId;
     String feedReferenceId;

     String notificationTitle;
     String notificationMessage;

     Set<FeatureEventAction> actions;
}
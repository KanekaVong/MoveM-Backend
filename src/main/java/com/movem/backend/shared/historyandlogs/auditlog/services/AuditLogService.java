package com.movem.backend.shared.historyandlogs.auditlog.services;

import com.movem.backend.shared.historyandlogs.auditlog.dtos.responses.AuditLogResponse;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.commons.enums.HistoryandLogs.AuditSeverity;

import java.util.List;

public interface AuditLogService {

    void createLog(
            Activity activity,
            User user,
            ActivityFeedEvent eventType,
            AuditCategory category,
            AuditSeverity severity,
            String fieldChanged,
            String description,
            String oldValue,
            String newValue
    );

    void createDeletedActivityLog(
            String activityId,
            String activityName,
            User user,
            ActivityFeedEvent eventType,
            AuditCategory category,
            AuditSeverity severity,
            String fieldChanged,
            String description,
            String oldValue,
            String newValue
    );

    List<AuditLogResponse> getAuditLogs(
            String activityId
    );

    List<AuditLogResponse> getFriendAuditLogs();

    List<AuditLogResponse> getGroupAuditLogs(
            String activityId
    );

    List<AuditLogResponse> getMyAuditLogs();
}

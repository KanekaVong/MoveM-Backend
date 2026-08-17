package com.movem.backend.Service.SharedServices;

import com.movem.backend.Dto.response.AuditLogResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;

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

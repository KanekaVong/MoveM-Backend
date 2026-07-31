package com.movem.backend.service.SharedServices;

import com.movem.backend.dto.response.AuditLogResponse;
import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
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

    List<AuditLogResponse> getAuditLogs(
            String activityId
    );

    List<AuditLogResponse> getFriendAuditLogs();

    List<AuditLogResponse> getGroupAuditLogs(
            String activityId
    );

    List<AuditLogResponse> getMyAuditLogs();
}

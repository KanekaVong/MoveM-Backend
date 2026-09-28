package com.movem.backend.shared.historyandlogs.auditlog.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.commons.enums.HistoryandLogs.AuditSeverity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLogResponse {
     Long id;
     Integer userId;
     String username;
     AuditCategory category;
     AuditSeverity severity;
     String fieldChanged;
     ActivityFeedEvent eventType;
     String description;
     String oldValue;
     String newValue;
     LocalDateTime createdAt;

}

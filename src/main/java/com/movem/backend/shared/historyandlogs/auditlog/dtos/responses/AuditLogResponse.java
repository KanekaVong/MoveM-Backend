package com.movem.backend.shared.historyandlogs.auditlog.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.commons.enums.HistoryandLogs.AuditSeverity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

    private Long id;

    private Integer userId;

    private String username;

    private AuditCategory category;

    private AuditSeverity severity;

    private String fieldChanged;

    private ActivityFeedEvent eventType;

    private String description;

    private String oldValue;

    private String newValue;

    private LocalDateTime createdAt;

}

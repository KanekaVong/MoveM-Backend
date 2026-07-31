package com.movem.backend.dto.response;

import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
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

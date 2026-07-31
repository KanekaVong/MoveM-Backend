package com.movem.backend.mapper.SharedMapper;

import com.movem.backend.dto.response.AuditLogResponse;
import com.movem.backend.entity.FeedsAndLogs.AuditLog;
import com.movem.backend.mapper.AbstractBaseMapper;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@Component
public class AuditLogMapper
        extends AbstractBaseMapper<AuditLog, AuditLogResponse> {

    @Override
    public AuditLogResponse toResponse(AuditLog log) {

        if (log == null) {
            return null;
        }

        return AuditLogResponse.builder()
                .id(log.getId())
                .userId(log.getUser().getId())
                .username(log.getUser().getUsername())
                .eventType(log.getEventType())
                .category(log.getCategory())
                .severity(log.getSeverity())
                .fieldChanged(log.getFieldChanged())
                .description(log.getDescription())
                .oldValue(log.getOldValue())
                .newValue(log.getNewValue())
                .createdAt(log.getCreatedAt())
                .build();
    }

}

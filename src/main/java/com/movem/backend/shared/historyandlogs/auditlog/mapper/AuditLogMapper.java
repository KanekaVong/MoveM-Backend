package com.movem.backend.shared.historyandlogs.auditlog.mapper;

import com.movem.backend.shared.historyandlogs.auditlog.dtos.responses.AuditLogResponse;
import com.movem.backend.shared.historyandlogs.auditlog.entities.AuditLog;
import com.movem.backend.commons.BaseMapper.AbstractBaseMapper;
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

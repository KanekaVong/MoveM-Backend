package com.movem.backend.mapper.SharedMapper;

import com.movem.backend.dto.response.AuditLogResponse;
import com.movem.backend.entity.FeedsAndLogs.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLogResponse toResponse(
            AuditLog log
    ) {

        AuditLogResponse response =
                new AuditLogResponse();

        response.setId(log.getId());

        response.setUserId(log.getUser().getId());

        response.setUsername(log.getUser().getUsername());

        response.setEventType(log.getEventType());

        response.setCategory(log.getCategory());

        response.setSeverity(log.getSeverity());

        response.setFieldChanged(log.getFieldChanged());

        response.setDescription(log.getDescription());

        response.setOldValue(log.getOldValue());

        response.setNewValue(log.getNewValue());

        response.setCreatedAt(log.getCreatedAt());

        return response;

    }

}

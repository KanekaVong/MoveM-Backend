package com.movem.backend.Controller.SharedController;


import com.movem.backend.Dto.response.AuditLogResponse;
import com.movem.backend.Service.SharedServices.AuditLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@Tag(
        name = "Social - Audit-Logs",
        description = "Past Actions made by all users"
)
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/me")
    public List<AuditLogResponse> getMyAuditLogs() {
        return auditLogService.getMyAuditLogs();
    }

    @GetMapping("/friends")
    public List<AuditLogResponse> getFriendAuditLogs() {
        return auditLogService.getFriendAuditLogs();
    }

    @GetMapping("/groups/{activityId}")
    public List<AuditLogResponse> getGroupAuditLogs(
            @PathVariable String activityId
    ) {
        return auditLogService.getGroupAuditLogs(activityId);
    }

    @GetMapping("/{activityId}")
    public List<AuditLogResponse> getAuditLogs(
            @PathVariable String activityId
    ) {
        return auditLogService.getAuditLogs(activityId);
    }

}
package com.movem.backend.Service.Implement.AuditLog;

import com.movem.backend.Dto.response.AuditLogResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.FeedsAndLogs.AuditLog;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.SharedMapper.AuditLogMapper;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Repository.SharedRepository.AuditLogRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.SharedServices.AuditLogService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ActivityRepository activityRepository;
    private final CurrentUserService currentUserService;
    private final AuditLogMapper auditLogMapper;

    @Override
    public void createLog(
            Activity activity,
            User user,
            ActivityFeedEvent eventType,
            AuditCategory category,
            AuditSeverity severity,
            String fieldChanged,
            String description,
            String oldValue,
            String newValue
    ) {

        AuditLog auditLog = new AuditLog();

        auditLog.setActivity(activity);

        auditLog.setUser(user);

        auditLog.setEventType(eventType);

        auditLog.setCategory(category);

        auditLog.setSeverity(severity);

        auditLog.setFieldChanged(fieldChanged);

        auditLog.setDescription(description);

        auditLog.setOldValue(oldValue);

        auditLog.setNewValue(newValue);

        auditLog.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(auditLog);

    }

    public void createDeletedActivityLog(
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
    ) {

        AuditLog auditLog = new AuditLog();

        auditLog.setActivity(null);
        auditLog.setActivityId(activityId);
        auditLog.setActivityName(activityName);

        auditLog.setUser(user);
        auditLog.setEventType(eventType);
        auditLog.setCategory(category);
        auditLog.setSeverity(severity);
        auditLog.setFieldChanged(fieldChanged);
        auditLog.setDescription(description);
        auditLog.setOldValue(oldValue);
        auditLog.setNewValue(newValue);
        auditLog.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getMyAuditLogs() {

        User currentUser = currentUserService.getCurrentUser();

        return auditLogRepository
                .findByUserOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(auditLogMapper::toResponse)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogs(
            String activityId
    ) {

        Activity activity = activityRepository
                .findById(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Activity not found."
                        )
                );

        return auditLogRepository
                .findByActivityOrderByCreatedAtDesc(activity)
                .stream()
                .map(auditLogMapper::toResponse)
                .toList();

    }

    @Override
    public List<AuditLogResponse> getFriendAuditLogs() {

        User currentUser = currentUserService.getCurrentUser();

        return auditLogRepository
                .findByUserAndCategoryOrderByCreatedAtDesc(
                        currentUser,
                        AuditCategory.FRIEND
                )
                .stream()
                .map(auditLogMapper::toResponse)
                .toList();
    }

    @Override
    public List<AuditLogResponse> getGroupAuditLogs(
            String activityId
    ) {

        Activity activity = activityRepository
                .findById(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Activity not found."
                        )
                );

        return auditLogRepository
                .findByActivityAndCategoryOrderByCreatedAtDesc(
                        activity,
                        AuditCategory.GROUP
                )
                .stream()
                .map(auditLogMapper::toResponse)
                .toList();

    }

}

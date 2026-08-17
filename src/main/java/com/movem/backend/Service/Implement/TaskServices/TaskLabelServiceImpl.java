package com.movem.backend.Service.Implement.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateTaskLabelRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateTaskLabelRequest;
import com.movem.backend.Dto.response.TaskResponses.TaskLabelResponse;
import com.movem.backend.Entity.Tasks.TaskLabel;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.Repository.TaskRepositories.TaskLabelRepository;
import com.movem.backend.Repository.AuthRepository.UserRepository;
import com.movem.backend.Service.SharedServices.AuditLogService;
import com.movem.backend.Service.TaskServices.TaskLabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskLabelServiceImpl implements TaskLabelService {

    private final TaskLabelRepository taskLabelRepository;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    @Override
    public TaskLabelResponse create(CreateTaskLabelRequest request) {

        User user = getCurrentUser();

        if (taskLabelRepository.existsByUserAndName(user, request.getName())) {
            throw new RuntimeException("Label already exists.");
        }

        TaskLabel label = new TaskLabel();
        label.setUser(user);
        label.setName(request.getName());
        label.setColor(request.getColor());
        label.setCreatedAt(LocalDateTime.now());

        TaskLabel savedLabel = taskLabelRepository.save(label);

        auditLogService.createLog(
                null,
                user,
                ActivityFeedEvent.LABEL_ADDED,
                AuditCategory.TASK,
                AuditSeverity.INFO,
                "label",
                "Created label.",
                null,
                savedLabel.getName()
        );

        return mapToResponse(savedLabel);
    }

    @Override
    public List<TaskLabelResponse> getMyLabels() {

        User user = getCurrentUser();

        return taskLabelRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TaskLabelResponse update(Integer id,
                                    UpdateTaskLabelRequest request) {

        User user = getCurrentUser();

        TaskLabel label = taskLabelRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Task label not found."));

        if (!label.getName().equalsIgnoreCase(request.getName())
                && taskLabelRepository.existsByUserAndName(user, request.getName())) {

            throw new RuntimeException("Label already exists.");
        }

        label.setName(request.getName());
        label.setColor(request.getColor());

        String oldName = label.getName();

        TaskLabel updatedLabel = taskLabelRepository.save(label);

        auditLogService.createLog(
                null,
                user,
                ActivityFeedEvent.LABEL_ADDED,
                AuditCategory.TASK,
                AuditSeverity.INFO,
                "label",
                "Updated label.",
                oldName,
                updatedLabel.getName()
        );

        return mapToResponse(updatedLabel);
    }

    @Override
    public void delete(Integer id) {

        User user = getCurrentUser();

        TaskLabel label = taskLabelRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Task label not found."));

        taskLabelRepository.delete(label);

        String oldName = label.getName();

        auditLogService.createLog(
                null,
                user,
                ActivityFeedEvent.LABEL_REMOVED,
                AuditCategory.TASK,
                AuditSeverity.WARNING,
                "label",
                "Deleted label.",
                oldName,
                null
        );
    }

    private TaskLabelResponse mapToResponse(TaskLabel label) {

        return TaskLabelResponse.builder()
                .id(label.getId())
                .name(label.getName())
                .color(label.getColor())
                .build();
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated user.");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found."));
    }
}
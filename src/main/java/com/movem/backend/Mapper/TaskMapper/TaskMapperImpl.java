package com.movem.backend.Mapper.TaskMapper;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Dto.response.TaskResponses.TaskResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Mapper.BaseMapper.AbstractBaseMapper;
import com.movem.backend.Repository.AttachmentRepository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMapperImpl
        extends AbstractBaseMapper<Task, TaskResponse>
        implements TaskMapper {

    private final TaskChecklistMapper checklistMapper;
    private final TaskReminderMapper reminderMapper;
    private final TaskLabelMapper labelMapper;
    private final AttachmentRepository attachmentRepository;

    @Override
    public TaskResponse toResponse(Task task) {

        if (task == null) {
            return null;
        }

        Activity activity = task.getActivity();
        if (activity == null) {
            return null;
        }

        int totalChecklistItems = task.getChecklists() != null ? task.getChecklists().size() : 0;

        int completedChecklistItems = task.getChecklists() != null
                ? (int) task.getChecklists().stream()
                        .filter(c -> Boolean.TRUE.equals(c.getIsCompleted()))
                        .count()
                : 0;

        int checklistProgress = totalChecklistItems == 0
                ? 0
                : (completedChecklistItems * 100) / totalChecklistItems;

        // Resolve parent activity info (e.g., Parent Trip or Parent Task)
        String parentActivityId = null;
        String parentActivityName = null;
        if (activity.getParentActivity() != null) {
            parentActivityId = activity.getParentActivity().getId();
            parentActivityName = activity.getParentActivity().getActivityName();
        }

        // Map attachments
        List<AttachmentResponse> attachmentResponses = attachmentRepository != null
                ? attachmentRepository.findByTaskAndDeletedAtIsNull(task).stream()
                        .map(att -> AttachmentResponse.builder()
                                .id(att.getId())
                                .originalFileName(att.getOriginalFileName())
                                .fileType(att.getFileType())
                                .fileSize(att.getFileSize())
                                .filePath(att.getFilePath())
                                .uploadedBy(att.getUploadedBy() != null ? att.getUploadedBy().getId() : null)
                                .createdAt(att.getCreatedAt())
                                .build())
                        .toList()
                : Collections.emptyList();

        return TaskResponse.builder()
                .activityId(activity.getId())
                .activityName(activity.getActivityName())
                .description(activity.getDescription())
                .status(activity.getStatus())
                .priority(task.getPriority())
                .recurring(task.getIsRecurring())
                .recurringType(task.getRecurringType())
                .startActivity(activity.getStartActivity())
                .deadline(activity.getDeadline())
                .parentActivityId(parentActivityId)
                .parentActivityName(parentActivityName)
                .labels(
                        activity.getLabels() != null
                                ? labelMapper.toResponseList(new ArrayList<>(activity.getLabels()))
                                : Collections.emptyList()
                )
                .checklists(
                        task.getChecklists() != null
                                ? checklistMapper.toResponseList(task.getChecklists())
                                : Collections.emptyList()
                )
                .reminders(
                        task.getReminders() != null
                                ? reminderMapper.toResponseList(task.getReminders())
                                : Collections.emptyList()
                )
                .totalChecklistItems(totalChecklistItems)
                .completedChecklistItems(completedChecklistItems)
                .checklistProgress(checklistProgress)
                .attachments(attachmentResponses)
                .build();
    }

    @Override
    public List<TaskResponse> toResponseList(List<Task> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}
package com.movem.backend.task.mappers.impl;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.checklist.mapper.ChecklistMapper;
import com.movem.backend.shared.group.dtos.responses.GroupMemberResponse;
import com.movem.backend.shared.reminder.mapper.ReminderMapper;
import com.movem.backend.task.dtos.responses.TaskResponse;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.shared.group.entities.ActivityGroup;
import com.movem.backend.task.entities.Task;
import com.movem.backend.commons.BaseMapper.AbstractBaseMapper;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.shared.group.repositories.GroupRepository;
import com.movem.backend.shared.group.repositories.GroupMemberRepository;
import com.movem.backend.shared.attachment.services.AttachmentService;
import com.movem.backend.task.mappers.TaskLabelMapper;
import com.movem.backend.task.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMapperImpl extends AbstractBaseMapper<Task, TaskResponse> implements TaskMapper {

    private final ChecklistMapper checklistMapper;
    private final ReminderMapper reminderMapper;
    private final TaskLabelMapper labelMapper;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

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

        int completedChecklistItems = task.getChecklists() != null ? (int) task.getChecklists().stream().filter(c -> Boolean.TRUE.equals(c.getIsCompleted())).count() : 0;

        int checklistProgress = totalChecklistItems == 0 ? 0 : (completedChecklistItems * 100) / totalChecklistItems;

        List<AttachmentResponse> attachmentResponses =
                attachmentRepository != null
                        ? attachmentRepository
                        .findByTaskAndDeletedAtIsNull(task)
                        .stream()
                        .map(attachmentService::toResponse)
                        .toList()
                        : Collections.emptyList();

        List<GroupMemberResponse> members = Collections.emptyList();

        ActivityGroup group = groupRepository.findByActivity(activity).orElse(null);

        if (group != null) {
            members = groupMemberRepository
                    .findByActivityGroup(group)
                    .stream()
                    .map(member -> {
                        GroupMemberResponse response = new GroupMemberResponse();

                        response.setUserId(member.getUser().getId());
                        response.setUsername(member.getUser().getUsername());
                        response.setFirstname(member.getUser().getFirstname());
                        response.setLastname(member.getUser().getLastname());
                        response.setProfilePic(member.getUser().getProfilePic());
                        response.setRole(member.getRole());
                        response.setJoinedAt(member.getJoinedAt());

                        return response;
                    }).toList();
        }

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
                .labels(activity.getLabels() != null ? labelMapper.toResponseList(new ArrayList<>(activity.getLabels())) : Collections.emptyList())
                .checklists(task.getChecklists() != null ? checklistMapper.toResponseList(task.getChecklists()) : Collections.emptyList())
                .reminders(task.getReminders() != null ? reminderMapper.toResponseList(task.getReminders()) : Collections.emptyList())
                .totalChecklistItems(totalChecklistItems)
                .completedChecklistItems(completedChecklistItems)
                .checklistProgress(checklistProgress)

                .attachments(attachmentResponses)

                .members(members)

                .build();
    }

    @Override
    public List<TaskResponse> toResponseList(List<Task> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
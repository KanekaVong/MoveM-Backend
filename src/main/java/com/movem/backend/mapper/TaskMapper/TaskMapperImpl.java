package com.movem.backend.mapper.TaskMapper;

import com.movem.backend.dto.response.TaskResponses.TaskResponse;
import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Tasks.Task;
import com.movem.backend.entity.Tasks.TaskChecklist;
import com.movem.backend.mapper.AbstractBaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMapperImpl implements TaskMapper {

    private final TaskChecklistMapper checklistMapper;
    private final TaskReminderMapper reminderMapper;
    private final TaskLabelMapper labelMapper;


    @Override
    public TaskResponse toResponse(Task task) {

        if (task == null) {
            return null;
        }

        Activity activity = task.getActivity();

        int progress = 0;

        int totalChecklistItems = task.getChecklists().size();

        int completedChecklistItems =
                (int) task.getChecklists()
                        .stream()
                        .filter(c -> Boolean.TRUE.equals(c.getIsCompleted()))
                        .count();

        int checklistProgress =
                totalChecklistItems == 0
                        ? 0
                        : (completedChecklistItems * 100) / totalChecklistItems;

        return TaskResponse.builder()

                .activityId(activity.getId())
                .activityName(activity.getActivityName())

                .labels(
                        labelMapper.toResponseList(
                                new ArrayList<>(activity.getLabels())
                        )
                )

                .checklists(
                        checklistMapper.toResponseList(
                                task.getChecklists()
                        )
                )

                .reminders(
                        reminderMapper.toResponseList(
                                task.getReminders()
                        )
                )
                .totalChecklistItems(totalChecklistItems)
                .completedChecklistItems(completedChecklistItems)
                .checklistProgress(checklistProgress)

                .description(activity.getDescription())
                .status(activity.getStatus())
                .priority(task.getPriority())
                .recurring(task.getIsRecurring())
                .startActivity(activity.getStartActivity())
                .deadline(activity.getDeadline())

                .build();
    }

    @Override
    public List<TaskResponse> toResponseList(List<Task> entities) {
        return List.of();
    }

}
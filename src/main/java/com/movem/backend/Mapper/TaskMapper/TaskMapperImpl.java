package com.movem.backend.Mapper.TaskMapper;

import com.movem.backend.Dto.response.TaskResponses.TaskResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Tasks.Task;
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
                .recurringType(task.getRecurringType())
                .startActivity(activity.getStartActivity())
                .deadline(activity.getDeadline())

                .build();
    }

    @Override
    public List<TaskResponse> toResponseList(List<Task> entities) {
        return List.of();
    }

}
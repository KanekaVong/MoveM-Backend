package com.movem.backend.mapper.TaskMapper;

import com.movem.backend.dto.response.TaskResponses.TaskReminderResponse;
import com.movem.backend.entity.Tasks.TaskReminder;
import com.movem.backend.mapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskReminderMapper
        extends AbstractBaseMapper<TaskReminder, TaskReminderResponse> {

    @Override
    public TaskReminderResponse toResponse(TaskReminder reminder) {

        if (reminder == null) {
            return null;
        }

        return TaskReminderResponse.builder()
                .id(reminder.getId())
                .remindAt(reminder.getRemindAt())
                .type(reminder.getType())
                .sent(reminder.getIsSent())
                .build();
    }
}
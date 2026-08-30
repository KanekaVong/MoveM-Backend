package com.movem.backend.Mapper.TaskMapper;

import com.movem.backend.Dto.response.TaskResponses.TaskReminderResponse;
import com.movem.backend.Entity.Tasks.TaskReminder;
import com.movem.backend.Mapper.BaseMapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

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
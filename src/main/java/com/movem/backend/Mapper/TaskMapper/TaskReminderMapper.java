package com.movem.backend.Mapper.TaskMapper;

import com.movem.backend.Dto.response.TaskResponses.ReminderResponse;
import com.movem.backend.Entity.Tasks.Reminder;
import com.movem.backend.Mapper.BaseMapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskReminderMapper
        extends AbstractBaseMapper<Reminder, ReminderResponse> {

    @Override
    public ReminderResponse toResponse(Reminder reminder) {

        if (reminder == null) {
            return null;
        }

        return ReminderResponse.builder()
                .id(reminder.getId())
                .remindAt(reminder.getRemindAt())
                .type(reminder.getType())
                .sent(reminder.getIsSent())
                .build();
    }
}
package com.movem.backend.shared.reminder.mapper;

import com.movem.backend.shared.reminder.dtos.responses.ReminderResponse;
import com.movem.backend.shared.reminder.entities.Reminder;
import com.movem.backend.commons.BaseMapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

@Component
public class ReminderMapper
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
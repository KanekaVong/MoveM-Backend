package com.movem.backend.shared.reminder.dtos.requests;

import com.movem.backend.commons.enums.shared.ReminderType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateReminderRequest {

    @NotNull(message = "Reminder time is required.")
    private LocalDateTime remindAt;

    @NotNull(message = "Reminder type is required.")
    private ReminderType type;

}
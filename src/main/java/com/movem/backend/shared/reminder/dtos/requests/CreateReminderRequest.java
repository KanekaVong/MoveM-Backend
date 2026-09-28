package com.movem.backend.shared.reminder.dtos.requests;

import com.movem.backend.commons.enums.shared.ReminderType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReminderRequest {
    @NotNull(message = "Reminder time is required.")
    LocalDateTime remindAt;
    @NotNull(message = "Reminder type is required.")
    ReminderType type;

}
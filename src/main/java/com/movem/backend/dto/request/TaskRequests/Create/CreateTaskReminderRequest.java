package com.movem.backend.dto.request.TaskRequests.Create;

import com.movem.backend.model.enums.ReminderType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateTaskReminderRequest {

    @NotNull(message = "Reminder time is required.")
    private LocalDateTime remindAt;

    @NotNull(message = "Reminder type is required.")
    private ReminderType type;

}
package com.movem.backend.shared.reminder.dtos.requests;

import com.movem.backend.commons.enums.shared.ReminderType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReminderRequest {

    @NotNull
    private ReminderType type;

    @NotNull
    private LocalDateTime remindAt;

}

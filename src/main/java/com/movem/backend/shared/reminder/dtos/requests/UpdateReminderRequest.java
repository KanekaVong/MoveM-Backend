package com.movem.backend.shared.reminder.dtos.requests;

import com.movem.backend.commons.enums.shared.ReminderType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateReminderRequest {
    @NotNull
    ReminderType type;

    @NotNull
    LocalDateTime remindAt;
}

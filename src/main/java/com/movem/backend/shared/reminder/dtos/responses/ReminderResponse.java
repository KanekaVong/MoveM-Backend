package com.movem.backend.shared.reminder.dtos.responses;

import com.movem.backend.commons.enums.shared.ReminderType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReminderResponse {
     Integer id;
     LocalDateTime remindAt;
     ReminderType type;
     Boolean sent;
}

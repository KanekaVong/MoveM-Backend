package com.movem.backend.shared.reminder.dtos.responses;

import com.movem.backend.commons.enums.shared.ReminderType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderResponse {

    private Integer id;

    private LocalDateTime remindAt;

    private ReminderType type;

    private Boolean sent;

}

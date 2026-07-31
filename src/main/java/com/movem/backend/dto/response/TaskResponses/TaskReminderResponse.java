package com.movem.backend.dto.response.TaskResponses;

import com.movem.backend.model.enums.ReminderType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReminderResponse {

    private Integer id;

    private LocalDateTime remindAt;

    private ReminderType type;

    private Boolean sent;

}

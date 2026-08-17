package com.movem.backend.Dto.request.TaskRequests.Update;

import com.movem.backend.model.enums.ReminderType;
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
public class UpdateTaskReminderRequest {

    @NotNull
    private ReminderType type;

    @NotNull
    private LocalDateTime remindAt;

}

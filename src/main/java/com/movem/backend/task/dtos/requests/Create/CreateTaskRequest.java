package com.movem.backend.task.dtos.requests.Create;

import com.movem.backend.commons.enums.Task.Priority;
import com.movem.backend.commons.enums.Task.RecurringType;
import com.movem.backend.commons.Util.TaskUtil.TaskCreateSource;
import com.movem.backend.shared.reminder.dtos.requests.CreateReminderRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTaskRequest implements TaskCreateSource {
    @NotBlank
    String activityName;
    String description;
    LocalDateTime startActivity;
    LocalDateTime deadline;
    String parentActivityId;

    @NotNull
    Priority priority;
    Boolean isRecurring = false;
    RecurringType recurringType;
    Integer recurringInterval = 1;
    LocalDate recurringEndDate;
    List<Integer> labelIds;
    List<CreateChecklistItemRequest> checklists;
    List<CreateReminderRequest> reminders;
}
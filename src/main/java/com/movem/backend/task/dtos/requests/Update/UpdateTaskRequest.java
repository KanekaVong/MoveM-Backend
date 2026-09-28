package com.movem.backend.task.dtos.requests.Update;

import com.movem.backend.shared.checklist.dtos.requests.UpdateChecklistItemRequest;
import com.movem.backend.shared.reminder.dtos.requests.CreateReminderRequest;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Task.Priority;
import com.movem.backend.commons.enums.Task.RecurringType;
import com.movem.backend.commons.Util.BaseUtil.BaseActivityUpdateSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTaskRequest implements BaseActivityUpdateSource {
    @NotBlank
    String activityName;
    String description;
    LocalDateTime startActivity;
    LocalDateTime deadline;

    @NotNull
    Priority priority;

    @Nullable
    ActivityStatus status;
    Boolean isRecurring = false;
    RecurringType recurringType;
    Integer recurringInterval = 1;
    LocalDate recurringEndDate;
    List<Integer> labelIds;
    List<UpdateChecklistItemRequest> checklists;
    List<CreateReminderRequest> reminders;
}

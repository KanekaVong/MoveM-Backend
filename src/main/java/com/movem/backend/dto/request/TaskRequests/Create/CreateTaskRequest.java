package com.movem.backend.dto.request.TaskRequests.Create;

import com.movem.backend.model.enums.Priority;
import com.movem.backend.util.TaskUtil.TaskCreateSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest implements TaskCreateSource {

    @NotBlank
    private String activityName;

    private String description;

    private LocalDateTime startActivity;

    private LocalDateTime deadline;

    private String parentActivityId;

    @NotNull
    private Priority priority;

    private Boolean isRecurring = false;

    private List<Integer> labelIds;

    private List<CreateChecklistItemRequest> checklists;

    private List<CreateTaskReminderRequest> reminders;

}
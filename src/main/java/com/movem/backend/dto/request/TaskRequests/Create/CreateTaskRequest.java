package com.movem.backend.dto.request.TaskRequests.Create;

import com.movem.backend.model.enums.Priority;
import com.movem.backend.util.ActivityCreateSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest implements ActivityCreateSource {

    @NotBlank
    private String activityName;

    private String description;

    private LocalDateTime startActivity;

    private LocalDateTime deadline;

    private String locationName;

    private String locationAddress;

    private BigDecimal lat;

    private BigDecimal lng;

    private String googlePlaceId;

    private String coordinates;

    private String parentActivityId;

    @NotNull
    private Priority priority;

    private Boolean isRecurring = false;

    private List<Integer> labelIds;

    private List<CreateChecklistItemRequest> checklists;

    private List<CreateTaskReminderRequest> reminders;

}
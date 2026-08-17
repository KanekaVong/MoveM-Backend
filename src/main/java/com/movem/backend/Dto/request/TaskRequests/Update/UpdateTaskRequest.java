package com.movem.backend.Dto.request.TaskRequests.Update;

import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;
import com.movem.backend.model.enums.RecurringType;
import com.movem.backend.Util.Base.BaseActivityUpdateSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest implements BaseActivityUpdateSource {

    @NotBlank
    private String activityName;

    private String description;

    private LocalDateTime startActivity;

    private LocalDateTime deadline;

    @NotNull
    private Priority priority;

    @NotNull
    private ActivityStatus status;

    private Boolean isRecurring = false;

    private RecurringType recurringType;

    private Integer recurringInterval = 1;

    private LocalDate recurringEndDate;

    private List<Integer> labelIds;


}

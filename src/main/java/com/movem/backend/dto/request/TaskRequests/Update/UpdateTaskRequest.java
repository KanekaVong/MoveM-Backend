package com.movem.backend.dto.request.TaskRequests.Update;

import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;
import com.movem.backend.util.Base.BaseActivityUpdateSource;
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
public class UpdateTaskRequest implements BaseActivityUpdateSource {

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

    @NotNull
    private Priority priority;

    @NotNull
    private ActivityStatus status;

    private Boolean isRecurring;

    private List<Integer> labelIds;


}

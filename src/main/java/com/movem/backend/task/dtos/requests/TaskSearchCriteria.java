package com.movem.backend.task.dtos.requests;

import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Task.Priority;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearchCriteria {

    private String search;

    private ActivityStatus status;

    private Priority priority;

    private Integer labelId;

    private String sortBy;

    private String direction;

    private Boolean overdue;

    private Integer upcomingDays;

    private Boolean active;
}
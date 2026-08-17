package com.movem.backend.Dto.request.TaskRequests.Search;

import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;
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
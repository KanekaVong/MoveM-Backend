package com.movem.backend.task.dtos.requests;

import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Task.Priority;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskSearchCriteria {
     String search;
     ActivityStatus status;
     Priority priority;
     Integer labelId;
     String sortBy;
     String direction;
     Boolean overdue;
     Integer upcomingDays;
     Boolean active;
}
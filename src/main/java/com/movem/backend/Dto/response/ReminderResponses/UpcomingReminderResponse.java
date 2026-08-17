package com.movem.backend.Dto.response.ReminderResponses;

import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.model.enums.ReminderType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingReminderResponse {

    private String activityId;

    private String activityName;

    private ActivityType activityType;

    private LocalDateTime remindAt;

    private ReminderType reminderType;
}

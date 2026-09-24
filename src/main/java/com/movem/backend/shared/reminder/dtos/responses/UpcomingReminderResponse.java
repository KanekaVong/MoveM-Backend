package com.movem.backend.shared.reminder.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityType;
import com.movem.backend.commons.enums.shared.ReminderType;
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

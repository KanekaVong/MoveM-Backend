package com.movem.backend.shared.reminder.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityType;
import com.movem.backend.commons.enums.shared.ReminderType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpcomingReminderResponse {
     String activityId;
     String activityName;
     ActivityType activityType;
     LocalDateTime remindAt;
     ReminderType reminderType;
}

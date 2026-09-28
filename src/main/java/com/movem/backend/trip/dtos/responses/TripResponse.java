package com.movem.backend.trip.dtos.responses;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.checklist.dtos.responses.ChecklistResponse;
import com.movem.backend.shared.reminder.dtos.responses.ReminderResponse;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripResponse {
     String activityId;
     String activityName;
     String description;
     ActivityStatus status;
     LocalDateTime startActivity;
     LocalDateTime deadline;

     String locationName;
     String locationAddress;
     BigDecimal lat;
     BigDecimal lng;
     String googlePlaceId;

     String destination;

     Integer memberCount;

     BigDecimal totalBudget;

     List<TripStopResponse> stops;
     List<AttachmentResponse> attachments;
     AttachmentResponse coverPhoto;
     List<ChecklistResponse> checklists;
     List<ReminderResponse> reminders;
     List<TripBudgetResponse> budgets;
     List<TripPackingItemResponse> packingItems;
}

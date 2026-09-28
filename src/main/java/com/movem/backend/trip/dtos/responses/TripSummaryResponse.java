package com.movem.backend.trip.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripSummaryResponse {
     String activityId;
     String activityName;
     String destination;
     String locationName;
     LocalDateTime startActivity;
     LocalDateTime deadline;
     ActivityStatus status;
     Integer memberCount;
     BigDecimal totalAllocatedBudget;
     BigDecimal totalSpent;
     AttachmentResponse coverPhoto;
}
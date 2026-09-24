package com.movem.backend.trip.dtos.responses;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.checklist.dtos.responses.ChecklistResponse;
import com.movem.backend.shared.reminder.dtos.responses.ReminderResponse;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResponse {

    private String activityId;
    private String activityName;
    private String description;
    private ActivityStatus status;
    private LocalDateTime startActivity;
    private LocalDateTime deadline;

    private String locationName;
    private String locationAddress;
    private BigDecimal lat;
    private BigDecimal lng;
    private String googlePlaceId;

    private String destination;

    private Integer memberCount;

    private BigDecimal totalBudget;

    private List<TripStopResponse> stops;
    private List<AttachmentResponse> attachments;
    private AttachmentResponse coverPhoto;
    private List<ChecklistResponse> checklists;
    private List<ReminderResponse> reminders;
    private List<TripBudgetResponse> budgets;
    private List<TripPackingItemResponse> packingItems;
}

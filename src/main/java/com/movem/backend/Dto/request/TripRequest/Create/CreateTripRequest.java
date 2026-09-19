package com.movem.backend.Dto.request.TripRequest.Create;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.Dto.response.TaskResponses.ChecklistResponse;
import com.movem.backend.Dto.response.TaskResponses.ReminderResponse;
import com.movem.backend.Dto.response.TripResponses.TripResponse;
import com.movem.backend.Entity.Collaboration.ActivityGroup;
import com.movem.backend.Entity.Tasks.Checklist;
import com.movem.backend.Entity.Tasks.Reminder;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Entity.Trip.TripBudget;
import com.movem.backend.Util.TripUtil.TripCreateSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTripRequest implements TripCreateSource {
    @NotBlank
    private String activityName;
    private String description;
    @NotNull
    private LocalDateTime startActivity;
    private LocalDateTime deadline;
    private String locationName;
    private String locationAddress;
    private BigDecimal lat;
    private BigDecimal lng;
    private String googlePlaceId;
    private String coordinates;
    private String destination;
    private String parentActivityId;

    @Nullable
    private List<CreateChecklistItemRequest> checklistItems;
    private List<CreateTripStopRequest> stops;
    private List<CreateTripBudgetRequest> budgets;
    private List<CreateTripPackingItemRequest> packingItems;

}
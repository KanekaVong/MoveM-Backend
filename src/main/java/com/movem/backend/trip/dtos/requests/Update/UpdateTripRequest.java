package com.movem.backend.trip.dtos.requests.Update;

import com.movem.backend.task.dtos.requests.Create.CreateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.requests.UpdateChecklistItemRequest;
import com.movem.backend.trip.dtos.requests.Create.CreateTripBudgetRequest;
import com.movem.backend.trip.dtos.requests.Create.CreateTripPackingItemRequest;
import com.movem.backend.trip.dtos.requests.Create.CreateTripStopRequest;
import com.movem.backend.commons.Util.TripUtil.TripUpdateSource;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTripRequest implements TripUpdateSource {
    Integer id;

    @NotBlank
    String activityName;

    String description;

    LocalDateTime startActivity;
    LocalDateTime deadline;

    String locationName;
    String locationAddress;

    BigDecimal lat;
    BigDecimal lng;

    String googlePlaceId;
    String coordinates;
    String destination;
    ActivityStatus status;

    BigDecimal totalBudget;

    List<CreateTripStopRequest> addStops;
    List<UpdateTripStopRequest> updateStops;
    List<Integer> removeStopIds;

    List<CreateTripBudgetRequest> addBudgets;
    List<UpdateTripBudgetRequest> updateBudgets;
    List<Integer> removeBudgetIds;

    List<CreateTripPackingItemRequest> addPackingItems;
    List<Integer> removePackingItemIds;

    List<CreateChecklistItemRequest> addChecklistItems;
    List<UpdateChecklistItemRequest> updateChecklistItems;
}


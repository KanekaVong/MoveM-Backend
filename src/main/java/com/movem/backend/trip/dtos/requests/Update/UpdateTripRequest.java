package com.movem.backend.trip.dtos.requests.Update;

import com.movem.backend.task.dtos.requests.Create.CreateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.requests.UpdateChecklistItemRequest;
import com.movem.backend.trip.dtos.requests.Create.CreateTripBudgetRequest;
import com.movem.backend.trip.dtos.requests.Create.CreateTripPackingItemRequest;
import com.movem.backend.trip.dtos.requests.Create.CreateTripStopRequest;
import com.movem.backend.commons.Util.TripUtil.TripUpdateSource;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTripRequest implements TripUpdateSource {

    private Integer id;

    @NotBlank
    private String activityName;

    private String description;

    private LocalDateTime startActivity;
    private LocalDateTime deadline;

    private String locationName;
    private String locationAddress;

    private BigDecimal lat;
    private BigDecimal lng;

    private String googlePlaceId;
    private String coordinates;
    private String destination;
    private ActivityStatus status;

    private BigDecimal totalBudget;

    private List<CreateTripStopRequest> addStops;
    private List<UpdateTripStopRequest> updateStops;
    private List<Integer> removeStopIds;

    private List<CreateTripBudgetRequest> addBudgets;
    private List<UpdateTripBudgetRequest> updateBudgets;
    private List<Integer> removeBudgetIds;

    private List<CreateTripPackingItemRequest> addPackingItems;
    private List<Integer> removePackingItemIds;

    private List<CreateChecklistItemRequest> addChecklistItems;
    private List<UpdateChecklistItemRequest> updateChecklistItems;
}


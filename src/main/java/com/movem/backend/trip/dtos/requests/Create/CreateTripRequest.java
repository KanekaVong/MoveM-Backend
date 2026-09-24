package com.movem.backend.trip.dtos.requests.Create;

import com.movem.backend.task.dtos.requests.Create.CreateChecklistItemRequest;
import com.movem.backend.commons.Util.TripUtil.TripCreateSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


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
    private BigDecimal totalBudget;

    @Nullable
    private List<CreateChecklistItemRequest> checklistItems;
    private List<CreateTripStopRequest> stops;
    private List<CreateTripPackingItemRequest> packingItems;

}
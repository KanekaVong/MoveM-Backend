package com.movem.backend.trip.dtos.requests.Create;

import com.movem.backend.task.dtos.requests.Create.CreateChecklistItemRequest;
import com.movem.backend.commons.Util.TripUtil.TripCreateSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTripRequest implements TripCreateSource {
    @NotBlank
    String activityName;
    String description;
    @NotNull
    LocalDateTime startActivity;
    LocalDateTime deadline;
    String locationName;
    String locationAddress;
    BigDecimal lat;
    BigDecimal lng;
    String googlePlaceId;
    String coordinates;
    String destination;
    String parentActivityId;
    BigDecimal totalBudget;

    @Nullable
    List<CreateChecklistItemRequest> checklistItems;
    List<CreateTripStopRequest> stops;
    List<CreateTripPackingItemRequest> packingItems;

}
package com.movem.backend.Dto.request.TripRequest.Update;

import com.movem.backend.Util.TripUtil.TripUpdateSource;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTripRequest
        implements TripUpdateSource {

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

    private String flightNumber;

    private String hotelName;

    private ActivityStatus status;
}

package com.movem.backend.Dto.response.TripResponses;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripStopResponse {

    private Integer id;

    private String locationName;

    private Integer sequenceOrder;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    private String locationAddress;

    private BigDecimal lat;

    private BigDecimal lng;

    private String googlePlaceId;

    private Boolean isCompleted;
}

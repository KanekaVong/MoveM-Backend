package com.movem.backend.Dto.response.TripResponses.TripProgress;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TripProgressStopResponse {

    private Integer id;

    private Integer sequenceOrder;

    private String locationName;

    private String locationAddress;

    private BigDecimal lat;

    private BigDecimal lng;

    private Boolean isCompleted;
}
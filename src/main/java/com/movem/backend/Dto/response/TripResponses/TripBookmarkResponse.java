package com.movem.backend.Dto.response.TripResponses;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripBookmarkResponse {

    private Integer id;

    private String locationName;

    private String locationAddress;

    private BigDecimal lat;

    private BigDecimal lng;

    private String googlePlaceId;

    private LocalDateTime createdAt;
}

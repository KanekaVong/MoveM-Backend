package com.movem.backend.trip.dtos.responses;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripPackingItemResponse {
    private Integer id;
    private String itemName;
    private Boolean isPacked;
    private LocalDateTime createdAt;
}

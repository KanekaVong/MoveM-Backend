package com.movem.backend.Dto.response.TripResponses;

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

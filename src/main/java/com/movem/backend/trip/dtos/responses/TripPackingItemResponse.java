package com.movem.backend.trip.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripPackingItemResponse {
     Integer id;
     String itemName;
     Boolean isPacked;
     LocalDateTime createdAt;
}

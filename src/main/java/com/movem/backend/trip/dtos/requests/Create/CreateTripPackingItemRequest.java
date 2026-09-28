package com.movem.backend.trip.dtos.requests.Create;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTripPackingItemRequest {
    @NotBlank
    String itemName;
}

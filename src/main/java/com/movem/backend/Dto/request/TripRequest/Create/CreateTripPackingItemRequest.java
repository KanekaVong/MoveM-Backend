package com.movem.backend.Dto.request.TripRequest.Create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTripPackingItemRequest {

    @NotBlank
    private String itemName;
}

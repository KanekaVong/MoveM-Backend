package com.movem.backend.trip.dtos.requests.Update;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReorderTripStopsRequest {
    @NotEmpty
    List<Integer> stopIds;
}

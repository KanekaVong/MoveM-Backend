package com.movem.backend.trip.dtos.requests.Update;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReorderTripStopsRequest {

    // Stop IDs in the new visiting order — every existing stop must appear exactly once
    @NotEmpty
    private List<Integer> stopIds;
}

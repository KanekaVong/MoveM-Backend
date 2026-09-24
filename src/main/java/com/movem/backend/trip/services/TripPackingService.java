package com.movem.backend.trip.services;

import com.movem.backend.trip.dtos.requests.Create.CreateTripPackingItemRequest;
import com.movem.backend.trip.dtos.responses.TripPackingItemResponse;

import java.util.List;

public interface TripPackingService {

    TripPackingItemResponse addItem(String tripActivityId, CreateTripPackingItemRequest request);

    List<TripPackingItemResponse> getItems(String tripActivityId);

    TripPackingItemResponse togglePacked(String tripActivityId, Integer itemId);

    void removeItem(String tripActivityId, Integer itemId);
}

package com.movem.backend.Service.TripServices;

import com.movem.backend.Dto.request.TripRequest.Create.CreateTripPackingItemRequest;
import com.movem.backend.Dto.response.TripResponses.TripPackingItemResponse;

import java.util.List;

public interface TripPackingService {

    TripPackingItemResponse addItem(String tripActivityId, CreateTripPackingItemRequest request);

    List<TripPackingItemResponse> getItems(String tripActivityId);

    TripPackingItemResponse togglePacked(String tripActivityId, Integer itemId);

    void removeItem(String tripActivityId, Integer itemId);
}

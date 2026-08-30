package com.movem.backend.Controller.TripControllers;

import com.movem.backend.Dto.request.TripRequest.Create.CreateTripPackingItemRequest;
import com.movem.backend.Dto.response.TripResponses.TripPackingItemResponse;
import com.movem.backend.Service.TripServices.TripPackingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{activityId}/packing-items")
@Tag( name = "Trip - Trip Packing Item",
        description = "Create trip, add collaborators, plan trips seamlessly")
@RequiredArgsConstructor
public class TripPackingItemController {

    private final TripPackingService tripPackingService;

    @PostMapping
    public ResponseEntity<TripPackingItemResponse> addItem(
            @PathVariable String activityId,
            @Valid @RequestBody CreateTripPackingItemRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripPackingService.addItem(activityId, request));
    }

    @GetMapping
    public ResponseEntity<List<TripPackingItemResponse>> getItems(@PathVariable String activityId) {
        return ResponseEntity.ok(tripPackingService.getItems(activityId));
    }

    @PatchMapping("/{itemId}/toggle")
    public ResponseEntity<TripPackingItemResponse> togglePacked(
            @PathVariable String activityId,
            @PathVariable Integer itemId
    ) {
        return ResponseEntity.ok(tripPackingService.togglePacked(activityId, itemId));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable String activityId, @PathVariable Integer itemId) {
        tripPackingService.removeItem(activityId, itemId);
        return ResponseEntity.noContent().build();
    }
}

package com.movem.backend.Service.Implement.TripServices;

import com.movem.backend.Dto.request.TripRequest.Create.CreateTripPackingItemRequest;
import com.movem.backend.Dto.response.TripResponses.TripPackingItemResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Entity.Trip.TripPackingItem;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.TripRepositories.TripPackingItemRepository;
import com.movem.backend.Repository.TripRepositories.TripRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.SharedServices.ActivityPermissionService;
import com.movem.backend.Service.TripServices.TripPackingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripPackingServiceImpl implements TripPackingService {

    private final TripRepository tripRepository;
    private final TripPackingItemRepository tripPackingItemRepository;

    private final ActivityPermissionService activityPermissionService;
    private final CurrentUserService currentUserService;

    @Override
    public TripPackingItemResponse addItem(String tripActivityId, CreateTripPackingItemRequest request) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);
        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        TripPackingItem item = new TripPackingItem();
        item.setTrip(trip);
        item.setItemName(request.getItemName());
        item.setIsPacked(false);
        item.setCreatedAt(LocalDateTime.now());

        tripPackingItemRepository.save(item);

        return toResponse(item);
    }

    @Override
    public List<TripPackingItemResponse> getItems(String tripActivityId) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);
        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        return tripPackingItemRepository.findByTrip(trip).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TripPackingItemResponse togglePacked(String tripActivityId, Integer itemId) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);
        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        TripPackingItem item = findItemOrThrow(trip, itemId);
        item.setIsPacked(!Boolean.TRUE.equals(item.getIsPacked()));

        return toResponse(item);
    }

    @Override
    public void removeItem(String tripActivityId, Integer itemId) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);
        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        TripPackingItem item = findItemOrThrow(trip, itemId);
        tripPackingItemRepository.delete(item);
    }

    private TripPackingItemResponse toResponse(TripPackingItem item) {
        return TripPackingItemResponse.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .isPacked(item.getIsPacked())
                .createdAt(item.getCreatedAt())
                .build();
    }

    private Trip findTripOrThrow(String tripActivityId) {
        return tripRepository.findByActivityId(tripActivityId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + tripActivityId));
    }

    private TripPackingItem findItemOrThrow(Trip trip, Integer itemId) {
        return tripPackingItemRepository.findByIdAndTrip(itemId, trip)
                .orElseThrow(() -> new ResourceNotFoundException("Packing item not found: " + itemId));
    }
}

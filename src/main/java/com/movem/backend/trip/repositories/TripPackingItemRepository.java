package com.movem.backend.trip.repositories;

import com.movem.backend.trip.entities.Trip;
import com.movem.backend.trip.entities.TripPackingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripPackingItemRepository extends JpaRepository<TripPackingItem, Integer> {
    List<TripPackingItem> findByTrip(Trip trip);
    Optional<TripPackingItem> findByIdAndTrip(Integer id, Trip trip);
}

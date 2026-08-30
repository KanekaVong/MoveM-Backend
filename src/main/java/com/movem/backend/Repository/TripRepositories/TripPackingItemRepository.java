package com.movem.backend.Repository.TripRepositories;

import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Entity.Trip.TripPackingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripPackingItemRepository extends JpaRepository<TripPackingItem, Integer> {

    List<TripPackingItem> findByTrip(Trip trip);

    Optional<TripPackingItem> findByIdAndTrip(Integer id, Trip trip);
}

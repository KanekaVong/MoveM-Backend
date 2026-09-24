package com.movem.backend.trip.repositories;

import com.movem.backend.trip.entities.Trip;
import com.movem.backend.trip.entities.TripBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripBudgetRepository extends JpaRepository<TripBudget, Integer> {
    List<TripBudget> findByTrip(Trip trip);
    Optional<TripBudget> findByTripAndCategory(Trip trip, String category);
    Optional<TripBudget> findByIdAndTrip(Integer id, Trip trip);
}

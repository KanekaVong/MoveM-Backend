package com.movem.backend.Repository.TripRepositories;

import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Entity.Trip.TripBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripBudgetRepository extends JpaRepository<TripBudget, Integer> {

    List<TripBudget> findByTrip(Trip trip);

    Optional<TripBudget> findByIdAndTrip(Integer id, Trip trip);
}

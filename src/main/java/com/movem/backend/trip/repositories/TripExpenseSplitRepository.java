package com.movem.backend.trip.repositories;

import com.movem.backend.trip.entities.TripExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripExpenseSplitRepository extends JpaRepository<TripExpenseSplit, Integer> {
}

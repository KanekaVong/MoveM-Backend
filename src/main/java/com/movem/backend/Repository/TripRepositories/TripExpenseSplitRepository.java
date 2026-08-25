package com.movem.backend.Repository.TripRepositories;

import com.movem.backend.Entity.Trip.TripExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripExpenseSplitRepository extends JpaRepository<TripExpenseSplit, Integer> {
}

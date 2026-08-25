package com.movem.backend.Repository.TripRepositories;

import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Entity.Trip.TripStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripStopRepository extends JpaRepository<TripStop, Integer> {

    List<TripStop> findByTripOrderBySequenceOrderAsc(Trip trip);

    Optional<TripStop> findByIdAndTrip(Integer id, Trip trip);

    Integer countByTrip(Trip trip);

    Integer countByTripAndIsCompleted(
            Trip trip,
            Boolean isCompleted
    );

}

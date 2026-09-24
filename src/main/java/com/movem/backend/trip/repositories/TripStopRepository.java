package com.movem.backend.trip.repositories;

import com.movem.backend.trip.entities.Trip;
import com.movem.backend.trip.entities.TripStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TripStopRepository extends JpaRepository<TripStop, Integer> {
    List<TripStop> findByTripOrderBySequenceOrderAsc(Trip trip);
    Optional<TripStop> findByIdAndTrip(Integer id, Trip trip);
    Integer countByTrip(Trip trip);
    Integer countByTripAndIsCompleted(Trip trip, Boolean isCompleted);

    @Modifying
    @Query("""
    DELETE FROM TripStop s
    WHERE s.id = :stopId
      AND s.trip = :trip
""")
    void deleteByIdAndTrip(
            @Param("stopId") Integer stopId, @Param("trip") Trip trip);
}

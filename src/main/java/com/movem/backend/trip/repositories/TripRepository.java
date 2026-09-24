package com.movem.backend.trip.repositories;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.trip.entities.Trip;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface TripRepository
        extends JpaRepository<Trip, String>,
        JpaSpecificationExecutor<Trip> {

    @EntityGraph(attributePaths = {
            "activity",
            "stops"
    })

    Optional<Trip> findByActivityId(
            String activityId
    );

    @Transactional
    @Modifying
    void deleteByActivityId(String activityId);

    void deleteByActivity(Activity activity);
}

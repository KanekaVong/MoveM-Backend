package com.movem.backend.Repository.TripRepositories;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Trip.Trip;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
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

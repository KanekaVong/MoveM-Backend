package com.movem.backend.commons.Specification;

import com.movem.backend.fitness.workout.dtos.requests.FitnessWorkoutSearchRequest;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FitnessWorkoutSessionSpecification {

    public static Specification<FitnessWorkoutSession> filter(User user, FitnessWorkoutSearchRequest request) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("user"), user));

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (request.getSearch() != null && !request.getSearch().isBlank()) {

                String search = "%" + request.getSearch().trim().toLowerCase() + "%";
                predicates.add(cb.or(cb.like(cb.lower(root.get("workoutType")), search)));
            }

            if (request.getWorkoutType() != null) {
                predicates.add(cb.equal(root.get("workoutType"), request.getWorkoutType()));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            if (request.getTrackingMode() != null) {
                predicates.add(cb.equal(root.get("trackingMode"), request.getTrackingMode()));
            }


            if (request.getMinDistance() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("distance"), request.getMinDistance()));
            }


            if (request.getMaxDistance() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("distance"), request.getMaxDistance())
                );
            }

            if (request.getMinCalories() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("caloriesBurned"), request.getMinCalories()));
            }

            if (request.getMaxCalories() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("caloriesBurned"), request.getMaxCalories()));
            }

            if (request.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("finishedAt"), request.getStartDate().atStartOfDay()));
            }

            if (request.getEndDate() != null) {
                predicates.add(cb.lessThan(root.get("finishedAt"), request.getEndDate().plusDays(1).atStartOfDay()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
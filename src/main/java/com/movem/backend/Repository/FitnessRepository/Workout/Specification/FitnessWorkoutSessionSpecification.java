package com.movem.backend.Repository.FitnessRepository.Workout.Specification;

import com.movem.backend.Dto.request.FitnessRequest.Workout.FitnessWorkoutSearchRequest;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FitnessWorkoutSessionSpecification {

    public static Specification<FitnessWorkoutSession> filter(
            User user,
            FitnessWorkoutSearchRequest request
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            /*
             * Only current user's workouts
             */
            predicates.add(
                    cb.equal(
                            root.get("user"),
                            user
                    )
            );

            /*
             * Do not return deleted workouts
             */
            predicates.add(
                    cb.isNull(
                            root.get("deletedAt")
                    )
            );

            /*
             * Search
             */
            if (request.getSearch() != null
                    && !request.getSearch().isBlank()) {

                String search =
                        "%" +
                                request.getSearch()
                                        .trim()
                                        .toLowerCase() +
                                "%";

                predicates.add(
                        cb.or(

                                cb.like(
                                        cb.lower(
                                                root.get("workoutType")
                                        ),
                                        search
                                )

                        )
                );
            }

            /*
             * Workout type
             */
            if (request.getWorkoutType() != null) {

                predicates.add(
                        cb.equal(
                                root.get("workoutType"),
                                request.getWorkoutType()
                        )
                );
            }

            /*
             * Status
             */
            if (request.getStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                request.getStatus()
                        )
                );
            }

            /*
             * Tracking mode
             */
            if (request.getTrackingMode() != null) {

                predicates.add(
                        cb.equal(
                                root.get("trackingMode"),
                                request.getTrackingMode()
                        )
                );
            }

            /*
             * Minimum distance
             */
            if (request.getMinDistance() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("distance"),
                                request.getMinDistance()
                        )
                );
            }

            /*
             * Maximum distance
             */
            if (request.getMaxDistance() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("distance"),
                                request.getMaxDistance()
                        )
                );
            }

            /*
             * Minimum calories
             */
            if (request.getMinCalories() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("caloriesBurned"),
                                request.getMinCalories()
                        )
                );
            }

            /*
             * Maximum calories
             */
            if (request.getMaxCalories() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("caloriesBurned"),
                                request.getMaxCalories()
                        )
                );
            }

            /*
             * Start date
             */
            if (request.getStartDate() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("finishedAt"),
                                request.getStartDate()
                                        .atStartOfDay()
                        )
                );
            }

            /*
             * End date
             */
            if (request.getEndDate() != null) {

                predicates.add(
                        cb.lessThan(
                                root.get("finishedAt"),
                                request.getEndDate()
                                        .plusDays(1)
                                        .atStartOfDay()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }
}
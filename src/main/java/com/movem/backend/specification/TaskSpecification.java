package com.movem.backend.specification;

import com.movem.backend.entity.Tasks.Task;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;
import com.movem.backend.specification.helper.JpaJoinHelper;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TaskSpecification {

    public static Specification<Task> belongsToUser(User user) {

        return (root, query, criteriaBuilder) -> {

            Join<?,?> activity =
                    JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.equal(
                    activity.get("user"),
                    user
            );
        };
    }

    public static Specification<Task> notCompleted() {

        return (root, query, cb) -> {

            Join<?,?> activity =
                    JpaJoinHelper.joinActivity(root);

            return cb.notEqual(
                    activity.get("status"),
                    ActivityStatus.COMPLETE
            );
        };
    }

    public static Specification<Task> dueToday(
            LocalDateTime start,
            LocalDateTime end
    )
    {

        return (root, query, cb) -> {

            Join<?, ?> activity =
                    JpaJoinHelper.joinActivity(root);


            return cb.and(

                    cb.between(
                            activity.get("deadline"),
                            start,
                            end
                    ),

                    cb.notEqual(
                            activity.get("status"),
                            ActivityStatus.COMPLETE
                    ),

                    cb.notEqual(
                            activity.get("status"),
                            ActivityStatus.DELETED
                    )
            );
        };
    }

    public static Specification<Task> notDeleted() {

        return (root, query, criteriaBuilder) -> {

            Join<?,?> activity =
                    JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.notEqual(
                    activity.get("status"),
                    ActivityStatus.DELETED
            );
        };
    }

    public static Specification<Task> activeOnly() {

        return (root, query, cb) -> {

            Join<?, ?> activity =
                    JpaJoinHelper.joinActivity(root);


            return cb.and(

                    cb.notEqual(
                            activity.get("status"),
                            ActivityStatus.COMPLETE
                    ),

                    cb.notEqual(
                            activity.get("status"),
                            ActivityStatus.DELETED
                    )
            );
        };
    }

    public static Specification<Task> nameContains(
            String search
    ) {

        return (root, query, criteriaBuilder) -> {

            Join<?,?> activity =
                    JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(
                            activity.get("activityName")
                    ),
                    "%" + search.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Task> statusEquals(
            ActivityStatus status
    ) {

        return (root, query, criteriaBuilder) -> {

            Join<?,?> activity =
                    JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.equal(
                    activity.get("status"),
                    status
            );
        };
    }

    public static Specification<Task> priorityEquals(
            Priority priority
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("priority"),
                        priority
                );
    }

    public static Specification<Task> hasLabel(Integer labelId) {

        return (root, query, cb) -> {

            var subQuery = query.subquery(Integer.class);

            var activityLabel = subQuery.from(Task.class);

            Join<Object, Object> activity =
                    activityLabel.join("activity");

            Join<Object, Object> labels =
                    activity.join("labels");

            subQuery.select(cb.literal(1));

            subQuery.where(

                    cb.equal(
                            activityLabel.get("activity"),
                            root.get("activity")
                    ),

                    cb.equal(
                            labels.get("id"),
                            labelId
                    )
            );

            return cb.exists(subQuery);
        };
    }

    public static Specification<Task> deadlineBetween(
            LocalDateTime start,
            LocalDateTime end
    ) {

        return (root, query, cb) -> {

            Join<?, ?> activity =
                    JpaJoinHelper.joinActivity(root);


            return cb.between(
                    activity.get("deadline"),
                    start,
                    end
            );
        };
    }

    public static Specification<Task> isOverdue() {

        return (root, query, criteriaBuilder) -> {

            Join<?,?> activity =
                    JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.and(

                    // Deadline has passed
                    criteriaBuilder.lessThan(
                            activity.get("deadline"),
                            LocalDateTime.now()
                    ),

                    // Task is not completed
                    criteriaBuilder.notEqual(
                            activity.get("status"),
                            ActivityStatus.COMPLETE
                    ),

                    // Task is not deleted
                    criteriaBuilder.notEqual(
                            activity.get("status"),
                            ActivityStatus.DELETED
                    ),

                    // Deadline must exist
                    criteriaBuilder.isNotNull(
                            activity.get("deadline")
                    )
            );
        };
    }

    public static Specification<Task> upcoming(
            Integer days
    ) {

        return (root, query, criteriaBuilder) -> {

            Join<?,?> activity =
                    JpaJoinHelper.joinActivity(root);

            LocalDateTime now = LocalDateTime.now();

            LocalDateTime future =
                    now.plusDays(days);

            return criteriaBuilder.and(

                    // Deadline exists
                    criteriaBuilder.isNotNull(
                            activity.get("deadline")
                    ),

                    // Deadline has not passed
                    criteriaBuilder.greaterThanOrEqualTo(
                            activity.get("deadline"),
                            now
                    ),

                    // Deadline is within the requested range
                    criteriaBuilder.lessThanOrEqualTo(
                            activity.get("deadline"),
                            future
                    ),

                    // Not completed
                    criteriaBuilder.notEqual(
                            activity.get("status"),
                            ActivityStatus.COMPLETE
                    ),

                    // Not deleted
                    criteriaBuilder.notEqual(
                            activity.get("status"),
                            ActivityStatus.DELETED
                    )
            );
        };
    }

    public static Specification<Task> active() {

        return (root, query, criteriaBuilder) -> {

            Join<?,?> activity =
                    JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.and(

                    criteriaBuilder.or(

                            criteriaBuilder.equal(
                                    activity.get("status"),
                                    ActivityStatus.PENDING
                            ),

                            criteriaBuilder.equal(
                                    activity.get("status"),
                                    ActivityStatus.IN_PROGRESS
                            )
                    ),

                    criteriaBuilder.isNotNull(
                            activity.get("startActivity")
                    )
            );
        };
    }

    public static Specification<Task> completedBetween(
            LocalDateTime start,
            LocalDateTime end
    ) {

        return (root, query, criteriaBuilder) -> {

            Join<?, ?> activity =
                    JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.and(

                    criteriaBuilder.equal(
                            activity.get("status"),
                            ActivityStatus.COMPLETE
                    ),

                    criteriaBuilder.between(
                            activity.get("updatedAt"),
                            start,
                            end
                    )
            );
        };
    }

}
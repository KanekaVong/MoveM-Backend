package com.movem.backend.specification;

import com.movem.backend.entity.Tasks.Task;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TaskSpecification {

    private static Join<Object, Object> activityJoin(
            Root<Task> root
    ) {
        return root.join("activity", JoinType.INNER);
    }


    // =========================================================
    // USER
    // =========================================================

    public static Specification<Task> belongsToUser(User user) {

        return (root, query, criteriaBuilder) -> {

            Join<Object, Object> activity =
                    activityJoin(root);

            return criteriaBuilder.equal(
                    activity.get("user"),
                    user
            );
        };
    }


    // =========================================================
    // NOT DELETED
    // =========================================================

    public static Specification<Task> notDeleted() {

        return (root, query, criteriaBuilder) -> {

            Join<Object, Object> activity =
                    activityJoin(root);

            return criteriaBuilder.notEqual(
                    activity.get("status"),
                    ActivityStatus.DELETED
            );
        };
    }


    // =========================================================
    // SEARCH BY TASK NAME
    // =========================================================

    public static Specification<Task> nameContains(
            String search
    ) {

        return (root, query, criteriaBuilder) -> {

            Join<Object, Object> activity =
                    activityJoin(root);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(
                            activity.get("activityName")
                    ),
                    "%" + search.toLowerCase() + "%"
            );
        };
    }


    // =========================================================
    // FILTER BY STATUS
    // =========================================================

    public static Specification<Task> statusEquals(
            ActivityStatus status
    ) {

        return (root, query, criteriaBuilder) -> {

            Join<Object, Object> activity =
                    activityJoin(root);

            return criteriaBuilder.equal(
                    activity.get("status"),
                    status
            );
        };
    }


    // =========================================================
    // FILTER BY PRIORITY
    // =========================================================

    public static Specification<Task> priorityEquals(
            Priority priority
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("priority"),
                        priority
                );
    }


    // =========================================================
    // FILTER BY LABEL
    // =========================================================

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


    // =========================================================
    // FILTER OVERDUE TASKS
    // =========================================================

    public static Specification<Task> isOverdue() {

        return (root, query, criteriaBuilder) -> {

            Join<Object, Object> activity =
                    activityJoin(root);

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

    // =========================================================
    // FILTER UPCOMING TASKS
    // =========================================================

    public static Specification<Task> upcoming(
            Integer days
    ) {

        return (root, query, criteriaBuilder) -> {

            Join<Object, Object> activity =
                    activityJoin(root);

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

    // =========================================================
    // ACTIVE TASKS
    // =========================================================

    public static Specification<Task> active() {

        return (root, query, criteriaBuilder) -> {

            Join<Object, Object> activity =
                    activityJoin(root);

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

}
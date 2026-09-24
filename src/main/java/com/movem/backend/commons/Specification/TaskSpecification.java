package com.movem.backend.commons.Specification;

import com.movem.backend.shared.group.entities.ActivityGroup;
import com.movem.backend.task.entities.Task;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Task.Priority;
import com.movem.backend.commons.Specification.helper.JpaJoinHelper;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TaskSpecification {
    public static Specification<Task> belongsToUser(User user) {
        return (root, query, cb) -> {
            Join<?, ?> activity = JpaJoinHelper.joinActivity(root);

            // Owner
            var ownerCondition = cb.equal(activity.get("user"), user);

            // Collaborator/member
            var memberSubQuery = query.subquery(Integer.class);
            var activityGroup = memberSubQuery.from(ActivityGroup.class);

            Join<?, ?> members = activityGroup.join("members");

            memberSubQuery.select(cb.literal(1));
            memberSubQuery.where(cb.and(cb.equal(activityGroup.get("activity"), activity), cb.equal(members.get("user"), user)));

            var collaboratorCondition = cb.exists(memberSubQuery);
            return cb.or(ownerCondition, collaboratorCondition);
        };
    }

    public static Specification<Task> notCompleted() {
        return (root, query, cb) -> {
            Join<?,?> activity = JpaJoinHelper.joinActivity(root);
            return cb.notEqual(activity.get("status"), ActivityStatus.COMPLETE);
        };
    }

    public static Specification<Task> dueToday(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            Join<?, ?> activity = JpaJoinHelper.joinActivity(root);

            return cb.and(cb.between(activity.get("deadline"), start, end),
                    cb.notEqual(activity.get("status"), ActivityStatus.COMPLETE),
                    cb.notEqual(activity.get("status"), ActivityStatus.DELETED));
        };
    }

    public static Specification<Task> notDeleted() {
        return (root, query, criteriaBuilder) -> {
            Join<?,?> activity = JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.notEqual(activity.get("status"), ActivityStatus.DELETED);
        };
    }

    public static Specification<Task> activeOnly() {
        return (root, query, cb) -> {
            Join<?, ?> activity = JpaJoinHelper.joinActivity(root);
            return cb.and(
                    cb.notEqual(activity.get("status"), ActivityStatus.COMPLETE),
                    cb.notEqual(activity.get("status"), ActivityStatus.DELETED));
        };
    }

    public static Specification<Task> nameContains(String search) {
        return (root, query, criteriaBuilder) -> {
            Join<?,?> activity = JpaJoinHelper.joinActivity(root);
            return criteriaBuilder.like(criteriaBuilder.lower(activity.get("activityName")), "%" + search.toLowerCase() + "%");
        };
    }

    public static Specification<Task> statusEquals(ActivityStatus status) {
        return (root, query, criteriaBuilder) -> {
            Join<?,?> activity = JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.equal(activity.get("status"), status);
        };
    }

    public static Specification<Task> priorityEquals(Priority priority) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasLabel(Integer labelId) {

        return (root, query, cb) -> {

            var subQuery = query.subquery(Integer.class);
            var activityLabel = subQuery.from(Task.class);

            Join<Object, Object> activity = activityLabel.join("activity");

            Join<Object, Object> labels = activity.join("labels");

            subQuery.select(cb.literal(1));
            subQuery.where(cb.equal(activityLabel.get("activity"), root.get("activity")), cb.equal(labels.get("id"), labelId));

            return cb.exists(subQuery);
        };
    }

    public static Specification<Task> deadlineBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            Join<?, ?> activity = JpaJoinHelper.joinActivity(root);
            return cb.between(activity.get("deadline"), start, end);
        };
    }

    public static Specification<Task> isOverdue() {
        return (root, query, criteriaBuilder) -> {
            Join<?,?> activity = JpaJoinHelper.joinActivity(root);
            return criteriaBuilder.and(criteriaBuilder.lessThan(activity.get("deadline"), LocalDateTime.now()), criteriaBuilder.notEqual(activity.get("status"), ActivityStatus.COMPLETE),
                    criteriaBuilder.notEqual(activity.get("status"), ActivityStatus.DELETED), criteriaBuilder.isNotNull(activity.get("deadline")));
        };
    }

    public static Specification<Task> upcoming(Integer days) {
        return (root, query, criteriaBuilder) -> {

            Join<?,?> activity = JpaJoinHelper.joinActivity(root);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime future = now.plusDays(days);

            return criteriaBuilder.and(criteriaBuilder.isNotNull(activity.get("deadline")), criteriaBuilder.greaterThanOrEqualTo(activity.get("deadline"), now),
                    criteriaBuilder.lessThanOrEqualTo(activity.get("deadline"), future), criteriaBuilder.notEqual(activity.get("status"), ActivityStatus.COMPLETE),
                    criteriaBuilder.notEqual(activity.get("status"), ActivityStatus.DELETED));
        };
    }

    public static Specification<Task> active() {
        return (root, query, criteriaBuilder) -> {
            Join<?,?> activity = JpaJoinHelper.joinActivity(root);

            return criteriaBuilder.and(
                    criteriaBuilder.or(criteriaBuilder.equal(activity.get("status"), ActivityStatus.PENDING), criteriaBuilder.equal(activity.get("status"), ActivityStatus.IN_PROGRESS)),
                    criteriaBuilder.isNotNull(activity.get("startActivity")));
        };
    }

    public static Specification<Task> completedBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            Join<?, ?> activity = JpaJoinHelper.joinActivity(root);
            return criteriaBuilder.and(criteriaBuilder.equal(activity.get("status"), ActivityStatus.COMPLETE), criteriaBuilder.between(activity.get("updatedAt"), start, end));
        };
    }

}
package com.movem.backend.commons.Specification;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.shared.group.entities.ActivityGroup;
import com.movem.backend.shared.group.entities.GroupMember;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TripSpecification {
    private TripSpecification() {}

    private static Join<Trip, Activity> activityJoin(Root<Trip> root) {
        for (Join<?, ?> join : root.getJoins()) {
            if (join.getAttribute().getName().equals("activity")) {
                return (Join<Trip, Activity>) join;
            }
        }

        return root.join("activity", JoinType.INNER);
    }

    public static Specification<Trip> forUser(User user) {
        return (root, query, cb) -> {
            Join<Trip, Activity> activity = activityJoin(root);
            Subquery<Integer> memberSubquery = query.subquery(Integer.class);
            Root<GroupMember> groupMember = memberSubquery.from(GroupMember.class);

            Join<GroupMember, ActivityGroup> activityGroup = groupMember.join("activityGroup", JoinType.INNER);
            Join<ActivityGroup, Activity> groupActivity = activityGroup.join("activity", JoinType.INNER);

            memberSubquery.select(cb.literal(1)).where(cb.equal(groupActivity, activity), cb.equal(groupMember.get("user"), user));

            return cb.or(cb.equal(activity.get("user"), user), cb.exists(memberSubquery));
        };
    }


    public static Specification<Trip> notDeleted() {
        return (root, query, cb) ->
                cb.notEqual(activityJoin(root).get("status"), ActivityStatus.DELETED);
    }

    public static Specification<Trip> hasStatus(ActivityStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }

            return cb.equal(activityJoin(root).get("status"), status);
        };
    }

    public static Specification<Trip> matchesSearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase() + "%";

            Join<Trip, Activity> activity = activityJoin(root);

            return cb.or(cb.like(cb.lower(activity.get("activityName")), pattern), cb.like(cb.lower(cb.coalesce(root.get("destination"), "")), pattern));
        };
    }

    public static Specification<Trip> isUpcoming() {
        return (root, query, cb) -> cb.greaterThan(activityJoin(root).get("startActivity"), LocalDateTime.now());
    }

    public static Specification<Trip> isActive() {
        return (root, query, cb) -> {
            Join<Trip, Activity> activity = activityJoin(root);

            LocalDateTime now = LocalDateTime.now();
            return cb.and(cb.lessThanOrEqualTo(activity.get("startActivity"), now), cb.or(cb.isNull(activity.get("deadline")), cb.greaterThanOrEqualTo(activity.get("deadline"), now)));
        };
    }
}
package com.movem.backend.Service.Builder;

import com.movem.backend.Dto.request.TaskRequests.Search.TaskSearchCriteria;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.Specification.TaskSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSearchBuilder {

    public Specification<Task> buildSpecification(
            User user,
            TaskSearchCriteria criteria
    ) {

        Specification<Task> specification =
                TaskSpecification.belongsToUser(user)
                        .and(TaskSpecification.notDeleted());

        if (criteria.getSearch() != null &&
                !criteria.getSearch().isBlank()) {

            specification =
                    specification.and(
                            TaskSpecification.nameContains(
                                    criteria.getSearch()
                            )
                    );
        }

        if (criteria.getStatus() != null &&
                criteria.getStatus() != ActivityStatus.DELETED) {

            specification =
                    specification.and(
                            TaskSpecification.statusEquals(
                                    criteria.getStatus()
                            )
                    );
        }

        if (criteria.getPriority() != null) {

            specification =
                    specification.and(
                            TaskSpecification.priorityEquals(
                                    criteria.getPriority()
                            )
                    );
        }

        if (criteria.getLabelId() != null) {

            specification =
                    specification.and(
                            TaskSpecification.hasLabel(
                                    criteria.getLabelId()
                            )
                    );
        }

        if (Boolean.TRUE.equals(criteria.getOverdue())) {

            specification =
                    specification.and(
                            TaskSpecification.isOverdue()
                    );
        }

        if (criteria.getUpcomingDays() != null &&
                criteria.getUpcomingDays() > 0) {

            specification =
                    specification.and(
                            TaskSpecification.upcoming(
                                    criteria.getUpcomingDays()
                            )
                    );
        }

        if (Boolean.TRUE.equals(criteria.getActive())) {

            specification = specification.and(
                    TaskSpecification.active()
            );
        }

        return specification;
    }

    public Sort buildSort(
            TaskSearchCriteria criteria
    ) {

        if (criteria.getSortBy() == null) {
            return Sort.unsorted();
        }

        Sort.Direction direction =
                "desc".equalsIgnoreCase(criteria.getDirection())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return switch (criteria.getSortBy().toLowerCase()) {

            case "deadline" ->
                    Sort.by(direction, "activity.deadline");

            case "priority" ->
                    Sort.by(direction, "priority");

            case "name" ->
                    Sort.by(direction, "activity.activityName");

            case "created" ->
                    Sort.by(direction, "activity.createdAt");

            default ->
                    Sort.unsorted();
        };
    }


}
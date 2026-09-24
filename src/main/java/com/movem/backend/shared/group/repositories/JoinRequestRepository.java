package com.movem.backend.shared.group.repositories;

import com.movem.backend.shared.group.entities.ActivityGroup;
import com.movem.backend.shared.group.entities.JoinRequest;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.JoinRequestStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface JoinRequestRepository
        extends JpaRepository<JoinRequest, Long> {

    @EntityGraph(attributePaths = {
            "requester",
            "activityGroup",
            "activityGroup.activity"
    })
    Optional<JoinRequest> findByActivityGroupAndRequesterAndStatus(
            ActivityGroup activityGroup,
            User requester,
            JoinRequestStatus status
    );

    @EntityGraph(attributePaths = {
            "requester",
            "activityGroup",
            "activityGroup.activity"
    })
    List<JoinRequest> findByActivityGroupAndStatus(
            ActivityGroup activityGroup,
            JoinRequestStatus status
    );

    @EntityGraph(attributePaths = {
            "activityGroup",
            "activityGroup.activity"
    })
    List<JoinRequest> findByRequester(
            User requester
    );

    @EntityGraph(attributePaths = {
            "requester",
            "activityGroup",
            "activityGroup.activity"
    })
    Optional<JoinRequest>
    findByActivityGroupAndRequester(
            ActivityGroup activityGroup,
            User requester
    );

    @Transactional
    @Modifying
    void deleteByActivityGroup(ActivityGroup activityGroup);
}
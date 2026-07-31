package com.movem.backend.repository.GroupRepository;

import com.movem.backend.entity.Group.ActivityGroup;
import com.movem.backend.entity.Group.JoinRequest;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Group.InviteStatus;
import com.movem.backend.model.enums.Group.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinRequestRepository
        extends JpaRepository<JoinRequest, Long> {

    Optional<JoinRequest> findByActivityGroupAndRequesterAndStatus(
            ActivityGroup activityGroup,
            User requester,
            JoinRequestStatus status
    );

    List<JoinRequest> findByActivityGroupAndStatus(
            ActivityGroup activityGroup,
            JoinRequestStatus status
    );

    List<JoinRequest> findByRequester(User requester);

    Optional<JoinRequest>
    findByActivityGroupAndRequester(
            ActivityGroup activityGroup,
            User requester
    );}
package com.movem.backend.repository.GroupRepository;

import com.movem.backend.entity.Group.ActivityGroup;
import com.movem.backend.entity.Group.GroupInvite;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Group.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupInviteRepository
        extends JpaRepository<GroupInvite, Long> {

    Optional<GroupInvite> findByActivityGroupAndInviteeAndStatus(
            ActivityGroup activityGroup,
            User invitee,
            InviteStatus status
    );

    List<GroupInvite> findByInviteeAndStatusOrderByInvitedAtDesc(
            User invitee,
            InviteStatus status
    );

    List<GroupInvite> findByActivityGroup(ActivityGroup activityGroup);


    List<GroupInvite> findByActivityGroupAndStatus(
            ActivityGroup activityGroup,
            InviteStatus status
    );
}
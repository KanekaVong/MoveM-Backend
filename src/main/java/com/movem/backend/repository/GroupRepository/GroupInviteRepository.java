package com.movem.backend.repository.GroupRepository;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Group.ActivityGroup;
import com.movem.backend.entity.Group.GroupInvite;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Group.InviteStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface GroupInviteRepository
        extends JpaRepository<GroupInvite, Long> {

    Optional<GroupInvite> findByActivityGroupAndInviteeAndStatus(
            ActivityGroup activityGroup,
            User invitee,
            InviteStatus status
    );

    @EntityGraph(attributePaths = {
            "inviter",
            "invitee",
            "activityGroup",
            "activityGroup.activity"
    })
    List<GroupInvite> findByInviteeAndStatusOrderByInvitedAtDesc(
            User invitee,
            InviteStatus status
    );

    @EntityGraph(attributePaths = {
            "inviter",
            "invitee",
            "activityGroup",
            "activityGroup.activity"
    })
    List<GroupInvite> findByActivityGroup(
            ActivityGroup activityGroup
    );

    @EntityGraph(attributePaths = {
            "inviter",
            "invitee",
            "activityGroup",
            "activityGroup.activity"
    })
    List<GroupInvite> findByActivityGroupAndStatus(
            ActivityGroup activityGroup,
            InviteStatus status
    );

    @Transactional
    @Modifying
    void deleteByActivityGroup(ActivityGroup activityGroup);
}
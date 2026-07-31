package com.movem.backend.service.SharedServices;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Group.ActivityGroup;
import com.movem.backend.entity.User;
import com.movem.backend.exception.UnauthorizedActionException;
import com.movem.backend.model.enums.Group.GroupRole;
import com.movem.backend.repository.GroupRepository.GroupMemberRepository;
import com.movem.backend.repository.GroupRepository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityPermissionService {

    private final GroupRepository groupRepository;

    private final GroupMemberRepository groupMemberRepository;

    /**
     * Activity owner OR group member.
     */
    public void validateActivityAccess(
            Activity activity,
            User currentUser
    ) {

        // Activity owner
        if (activity.getUser().getId().equals(currentUser.getId())) {
            return;
        }

        ActivityGroup activityGroup = groupRepository
                .findByActivity(activity)
                .orElse(null);

        // No group exists -> not the owner -> deny
        if (activityGroup == null) {
            throw new UnauthorizedActionException(
                    "You do not have access to this activity."
            );
        }

        boolean isMember = groupMemberRepository
                .existsByActivityGroupAndUser(
                        activityGroup,
                        currentUser
                );

        if (!isMember) {
            throw new UnauthorizedActionException(
                    "You do not have access to this activity."
            );
        }
    }

    /**
     * Only activity owner.
     */
    public void validateActivityOwner(
            Activity activity,
            User currentUser
    ) {

        if (!activity.getUser().getId().equals(currentUser.getId())) {

            throw new UnauthorizedActionException(
                    "Only the activity owner can perform this action."
            );

        }

    }

    /**
     * Only group members (owner included).
     */
    public void validateGroupMember(
            Activity activity,
            User currentUser
    ) {

        validateActivityAccess(
                activity,
                currentUser
        );

    }

    public void validateCanEditActivity(
            Activity activity,
            User currentUser
    ) {

        validateActivityAccess(
                activity,
                currentUser
        );

    }

    public void validateGroupLeader(
            Activity activity,
            User currentUser
    ) {

        ActivityGroup activityGroup =
                groupRepository.findByActivity(activity)
                        .orElseThrow(() ->
                                new UnauthorizedActionException(
                                        "This activity is not a group."
                                ));

        boolean isLeader =
                groupMemberRepository.existsByActivityGroupAndUserAndRole(
                        activityGroup,
                        currentUser,
                        GroupRole.LEADER
                );

        if (!isLeader) {

            throw new UnauthorizedActionException(
                    "Only the group leader can perform this action."
            );

        }

    }

    public void validateCommentOwner(
            User commentOwner,
            User currentUser
    ) {

        if (!commentOwner.getId().equals(currentUser.getId())) {

            throw new UnauthorizedActionException(
                    "You can only modify your own comment."
            );

        }

    }


}
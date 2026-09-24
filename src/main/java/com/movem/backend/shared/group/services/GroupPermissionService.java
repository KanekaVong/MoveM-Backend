package com.movem.backend.shared.group.services;

import com.movem.backend.shared.group.entities.ActivityGroup;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Exception.UnauthorizedActionException;
import com.movem.backend.shared.group.repositories.GroupMemberRepository;
import com.movem.backend.commons.enums.shared.GroupRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupPermissionService {

    private final GroupMemberRepository groupMemberRepository;

    public void validateGroupMember(ActivityGroup group, User currentUser) {

        boolean isMember = groupMemberRepository.existsByActivityGroupAndUser(group, currentUser);

        if (!isMember) {
            throw new UnauthorizedActionException("You are not a member of this group.");
        }
    }
    public void validateGroupLeader(ActivityGroup group, User currentUser) {

        boolean isLeader =
                groupMemberRepository
                        .existsByActivityGroupAndUserAndRole(
                                group,
                                currentUser,
                                GroupRole.LEADER
                        );

        if (!isLeader) {

            throw new UnauthorizedActionException(
                    "Only the group leader can perform this action."
            );
        }
    }


    public void validateCanManageGroup(
            ActivityGroup group,
            User currentUser
    ) {

        validateGroupLeader(
                group,
                currentUser
        );
    }
}
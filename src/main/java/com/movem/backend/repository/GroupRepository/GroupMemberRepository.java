package com.movem.backend.repository.GroupRepository;

import com.movem.backend.entity.Group.ActivityGroup;
import com.movem.backend.entity.Group.GroupMember;
import com.movem.backend.entity.Group.GroupMemberId;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Group.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository
        extends JpaRepository<GroupMember, GroupMemberId> {

    List<GroupMember> findByActivityGroup(
            ActivityGroup activityGroup
    );

    List<GroupMember> findByUser(
            User user
    );

    Optional<GroupMember> findByActivityGroupAndUser(
            ActivityGroup activityGroup,
            User user
    );

    boolean existsByActivityGroupAndUser(
            ActivityGroup activityGroup,
            User user
    );

    long countByActivityGroup(
            ActivityGroup activityGroup
    );

    boolean existsByActivityGroupAndUserAndRole(
            ActivityGroup activityGroup,
            User user,
            GroupRole role
    );

}
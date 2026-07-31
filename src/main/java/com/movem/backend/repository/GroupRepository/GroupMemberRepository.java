package com.movem.backend.repository.GroupRepository;

import com.movem.backend.entity.Group.ActivityGroup;
import com.movem.backend.entity.Group.GroupMember;
import com.movem.backend.entity.Group.GroupMemberId;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Group.GroupRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository
        extends JpaRepository<GroupMember, GroupMemberId> {

    @EntityGraph(attributePaths = {
            "user"
    })
    List<GroupMember> findByActivityGroup(
            ActivityGroup activityGroup
    );

    @EntityGraph(attributePaths = {
            "activityGroup",
            "activityGroup.activity"
    })
    List<GroupMember> findByUser(
            User user
    );

    @EntityGraph(attributePaths = {
            "user",
            "activityGroup",
            "activityGroup.activity"
    })
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

    @Transactional
    @Modifying
    void deleteByActivityGroup(ActivityGroup activityGroup);

}
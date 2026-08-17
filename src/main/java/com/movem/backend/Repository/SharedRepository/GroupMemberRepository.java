package com.movem.backend.Repository.SharedRepository;

import com.movem.backend.Entity.Collaboration.ActivityGroup;
import com.movem.backend.Entity.Shared.GroupMember;
import com.movem.backend.Entity.Shared.GroupMemberId;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Collaboration.GroupRole;
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
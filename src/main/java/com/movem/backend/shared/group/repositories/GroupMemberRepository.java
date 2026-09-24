package com.movem.backend.shared.group.repositories;

import com.movem.backend.shared.group.entities.ActivityGroup;
import com.movem.backend.shared.group.entities.GroupMember;
import com.movem.backend.shared.group.entities.GroupMemberId;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.GroupRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    @EntityGraph(attributePaths = {"user"})
    List<GroupMember> findByActivityGroup(ActivityGroup activityGroup);

    @EntityGraph(attributePaths = {"activityGroup", "activityGroup.activity"})
    List<GroupMember> findByUser(User user);

    @EntityGraph(attributePaths = {"user", "activityGroup", "activityGroup.activity"})
    Optional<GroupMember> findByActivityGroupAndUser(ActivityGroup activityGroup, User user);

    boolean existsByActivityGroupAndUser(ActivityGroup activityGroup, User user);

    long countByActivityGroup(ActivityGroup activityGroup);

    boolean existsByActivityGroupAndUserAndRole(ActivityGroup activityGroup, User user, GroupRole role);

    @Transactional
    @Modifying
    void deleteByActivityGroup(ActivityGroup activityGroup);

}
package com.movem.backend.shared.group.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.GroupRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_members", indexes = {@Index(name = "idx_groupmember_group", columnList = "group_id"),
                @Index(name = "idx_groupmember_user", columnList = "user_id"),
                @Index(name = "idx_groupmember_role", columnList = "role")})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupMember {
    @EmbeddedId
    GroupMemberId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    ActivityGroup activityGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;
    @Enumerated(EnumType.STRING)
    GroupRole role;
    LocalDateTime joinedAt;

}
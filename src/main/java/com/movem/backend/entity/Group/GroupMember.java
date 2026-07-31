package com.movem.backend.entity.Group;

import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Group.GroupRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "group_members",
        indexes = {

                @Index(
                        name = "idx_groupmember_group",
                        columnList = "group_id"
                ),

                @Index(
                        name = "idx_groupmember_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_groupmember_role",
                        columnList = "role"
                )
        }
)
@Getter
@Setter
public class GroupMember {

    @EmbeddedId
    private GroupMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private ActivityGroup activityGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private GroupRole role;

    private LocalDateTime joinedAt;

}
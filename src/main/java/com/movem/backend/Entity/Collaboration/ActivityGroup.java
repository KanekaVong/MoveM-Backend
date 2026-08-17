package com.movem.backend.Entity.Collaboration;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Shared.GroupMember;
import com.movem.backend.Entity.Shared.JoinRequest;
import com.movem.backend.Entity.Auth.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "activity_groups",
        indexes = {
                @Index(name = "idx_activity_group_activity", columnList = "activity_id"),
                @Index(name = "idx_activity_group_creator", columnList = "created_by"),
                @Index(name = "idx_activity_group_token", columnList = "join_token")
        }
) //indexing is used to help optimize performances
@Getter
@Setter
public class ActivityGroup {
//Create trip -> create acitivty -> add friends -> Turn into a group trip -> Add into Activity Group -> Member kir add auto into group_members
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "activity_id",
            nullable = false,
            unique = true
    )
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by",
            nullable = false
    )
    private User createdBy;

    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "activityGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GroupMember> members;

    @OneToMany(
            mappedBy = "activityGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GroupInvite> invites;

    @OneToMany(
            mappedBy = "activityGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<JoinRequest> joinRequests;

    @Column(name = "join_token", unique = true)
    private String joinToken;

}
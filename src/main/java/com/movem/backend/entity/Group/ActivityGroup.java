package com.movem.backend.entity.Group;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activity_groups")
@Getter
@Setter
public class ActivityGroup {

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

    @OneToMany(mappedBy = "activityGroup")
    private List<GroupMember> members;

    @OneToMany(mappedBy = "activityGroup")
    private List<GroupInvite> invites;

    @OneToMany(mappedBy = "activityGroup")
    private List<JoinRequest> joinRequests;

    @Column(name = "join_token", unique = true)
    private String joinToken;

}
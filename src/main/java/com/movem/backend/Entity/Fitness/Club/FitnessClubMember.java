package com.movem.backend.Entity.Fitness.Club;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Fitness.FitnessClubRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "fitness_club_members",
        indexes = {

                @Index(
                        name = "idx_fitness_club_member_club",
                        columnList = "club_id"
                ),

                @Index(
                        name = "idx_fitness_club_member_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_fitness_club_member_role",
                        columnList = "role"
                )
        }
)
@Getter
@Setter
public class FitnessClubMember {

    @EmbeddedId
    private FitnessClubMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clubId")
    @JoinColumn(
            name = "club_id",
            nullable = false
    )
    private FitnessClub fitnessClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private FitnessClubRole role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
package com.movem.backend.fitness.club.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.Fitness.FitnessClubRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_club_members", indexes = {
                @Index(name = "idx_fitness_club_member_club", columnList = "club_id"),
                @Index(name = "idx_fitness_club_member_user", columnList = "user_id"),
                @Index(name = "idx_fitness_club_member_role", columnList = "role")})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessClubMember {
    @EmbeddedId
    FitnessClubMemberId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clubId")
    @JoinColumn(name = "club_id", nullable = false)
    FitnessClub fitnessClub;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    FitnessClubRole role;
    @Column(name = "joined_at")
    LocalDateTime joinedAt;
}
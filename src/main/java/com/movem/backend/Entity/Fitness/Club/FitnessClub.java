package com.movem.backend.Entity.Fitness.Club;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Fitness.ClubPrivacy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "fitness_clubs",
        indexes = {
                @Index(
                        name = "idx_fitness_club_creator",
                        columnList = "created_by"
                ),
                @Index(
                        name = "idx_fitness_club_privacy",
                        columnList = "privacy"
                ),
                @Index(
                        name = "idx_fitness_club_token",
                        columnList = "join_token"
                )
        }
)
@Getter
@Setter
public class FitnessClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            nullable = false,
            length = 150
    )
    private String name;

    @Column(
            columnDefinition = "TEXT"
    )
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by",
            nullable = false
    )
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private ClubPrivacy privacy;

    @Column(
            name = "join_token",
            unique = true,
            length = 100
    )
    private String joinToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
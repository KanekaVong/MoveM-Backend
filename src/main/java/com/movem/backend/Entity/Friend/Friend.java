package com.movem.backend.Entity.Friend;

import com.movem.backend.Entity.Auth.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "friend",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_friend_pair",
                        columnNames = {"user_one_id", "user_two_id"}
                )
        },
        indexes = {

                @Index(
                        name = "idx_friend_user_one",
                        columnList = "user_one_id"
                ),

                @Index(
                        name = "idx_friend_user_two",
                        columnList = "user_two_id"
                ),

                @Index(
                        name = "idx_friend_created",
                        columnList = "createdAt"
                )
        }
)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_one_id")
    private User userOne;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_two_id")
    private User userTwo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}

package com.movem.backend.social.friend.entities;

import com.movem.backend.authentication.entities.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "friend",
        uniqueConstraints = {@UniqueConstraint(name = "uk_friend_pair", columnNames = {"user_one_id", "user_two_id"})},
        indexes = {@Index(name = "idx_friend_user_one", columnList = "user_one_id"),
                @Index(name = "idx_friend_user_two", columnList = "user_two_id"),
                @Index(name = "idx_friend_created", columnList = "createdAt")})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_one_id")
    User userOne;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_two_id")
    User userTwo;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}

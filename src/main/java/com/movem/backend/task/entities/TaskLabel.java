package com.movem.backend.task.entities;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "task_labels", indexes = {@Index(name = "idx_tasklabel_user", columnList = "user_id"), @Index(name = "idx_tasklabel_name", columnList = "name")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false, length = 50)
    String name;

    @Column(nullable = false, length = 20)
    String color;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @ManyToMany(mappedBy = "labels")
    Set<Activity> activities = new HashSet<>();
}

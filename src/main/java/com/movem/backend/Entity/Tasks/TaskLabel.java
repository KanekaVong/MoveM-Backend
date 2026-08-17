package com.movem.backend.Entity.Tasks;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "task_labels",
        indexes = {

                @Index(
                        name = "idx_tasklabel_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_tasklabel_name",
                        columnList = "name"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String color;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "labels")
    private Set<Activity> activities = new HashSet<>();
}

package com.movem.backend.Repository.NotificationRepository;


import com.movem.backend.Entity.Shared.Notification;
import com.movem.backend.Entity.Auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(
            User user
    );

    List<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(
            User user
    );

    Long countByUserAndIsReadFalse(
            User user
    );

    Optional<Notification> findByIdAndUser(
            Long id,
            User user
    );

}

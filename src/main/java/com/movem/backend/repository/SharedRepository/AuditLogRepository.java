package com.movem.backend.repository.SharedRepository;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.FeedsAndLogs.AuditLog;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Audit.AuditCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByActivityOrderByCreatedAtDesc(
            Activity activity
    );

    List<AuditLog> findByUserAndCategoryOrderByCreatedAtDesc(
            User currentUser,
            AuditCategory category
    );

    List<AuditLog> findByActivityAndCategoryOrderByCreatedAtDesc(
            Activity activity,
            AuditCategory category
    );

    List<AuditLog> findByUserOrderByCreatedAtDesc(User user);
}

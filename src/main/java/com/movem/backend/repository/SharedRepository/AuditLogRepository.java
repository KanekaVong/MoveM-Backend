package com.movem.backend.repository.SharedRepository;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.FeedsAndLogs.AuditLog;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Audit.AuditCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Transactional
    @Modifying
    @Query("""
    UPDATE AuditLog a
    SET a.activity = null
    WHERE a.activity.id = :activityId
    """)
    int detachActivity(@Param("activityId") String activityId);
}

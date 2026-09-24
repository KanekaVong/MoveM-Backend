package com.movem.backend.shared.historyandlogs.auditlog.repositories;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.shared.historyandlogs.auditlog.entities.AuditLog;
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

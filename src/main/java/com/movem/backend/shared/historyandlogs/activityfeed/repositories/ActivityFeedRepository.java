package com.movem.backend.shared.historyandlogs.activityfeed.repositories;


import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.shared.historyandlogs.activityfeed.entities.ActivityFeed;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ActivityFeedRepository
        extends JpaRepository<ActivityFeed, Long> {

    @EntityGraph(attributePaths = {
            "user",
            "activity"
    })

    Page<ActivityFeed> findByActivityOrderByCreatedAtDesc(
            Activity activity,
            Pageable pageable
    );

    @Transactional
    @Modifying
    void deleteByActivity(Activity activity);
}
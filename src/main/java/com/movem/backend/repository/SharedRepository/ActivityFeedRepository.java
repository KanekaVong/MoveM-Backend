package com.movem.backend.repository.SharedRepository;


import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.FeedsAndLogs.ActivityFeed;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

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
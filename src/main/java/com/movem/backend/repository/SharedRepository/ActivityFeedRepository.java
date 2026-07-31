package com.movem.backend.repository.SharedRepository;


import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.FeedsAndLogs.ActivityFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityFeedRepository
        extends JpaRepository<ActivityFeed, Long> {

    List<ActivityFeed> findByActivityOrderByCreatedAtDesc(
            Activity activity
    );

}
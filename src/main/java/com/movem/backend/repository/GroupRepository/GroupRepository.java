package com.movem.backend.repository.GroupRepository;

import com.movem.backend.entity.Group.ActivityGroup;
import com.movem.backend.entity.Activity.Activity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface GroupRepository
        extends JpaRepository<ActivityGroup, Integer> {

    Optional<ActivityGroup> findByActivity(Activity activity);

    Optional<ActivityGroup> findByActivityId(String activityId);

    boolean existsByActivityId(String activityId);

    Optional<ActivityGroup> findByJoinToken(String joinToken);

    @Transactional
    @Modifying
    void deleteByActivity(Activity activity);
}

package com.movem.backend.shared.group.repositories;

import com.movem.backend.shared.group.entities.ActivityGroup;
import com.movem.backend.shared.activity.entities.Activity;
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

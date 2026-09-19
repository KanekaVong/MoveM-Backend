package com.movem.backend.Repository.TaskRepositories;

import java.util.List;
import java.util.Optional;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Tasks.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository
        extends JpaRepository<Task, String>,
        JpaSpecificationExecutor<Task> {

    @EntityGraph(attributePaths = {
            "activity",
            "checklists",
            "reminders",
            "activity.labels"
    })
    List<Task> findAll(
            Specification<Task> specification
    );

    Optional<Task> findByActivityId(String activityId);

    @Transactional
    @Modifying
    void deleteByActivityId(String activityId);

    void deleteByActivity(Activity activity);

    @Query("""
        SELECT t FROM Task t
        JOIN t.activity a
        WHERE a.parentActivity.id = :parentId
          AND a.status <> com.movem.backend.model.enums.Activity.ActivityStatus.DELETED
    """)
    List<Task> findTasksByParentActivityId(@Param("parentId") String parentId);
}
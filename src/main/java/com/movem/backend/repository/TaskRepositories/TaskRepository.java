package com.movem.backend.repository.TaskRepositories;

import java.util.List;
import java.util.Optional;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Tasks.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

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
}
package com.movem.backend.repository.TaskRepositories;

import com.movem.backend.entity.Tasks.Task;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TaskRepository
        extends JpaRepository<Task, String>,
        JpaSpecificationExecutor<Task> {

    Optional<Task> findByActivityId(String activityId);


    List<Task> findAllByActivityUserAndActivityStatusNot(
            User user,
            ActivityStatus status
    );

    Optional<Task> findByActivityIdAndActivityUser(
            String activityId,
            User user
    );

}
package com.movem.backend.repository.TaskRepositories;

import com.movem.backend.entity.Tasks.Task;
import com.movem.backend.entity.Tasks.TaskChecklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskChecklistRepository
        extends JpaRepository<TaskChecklist, Integer> {

    List<TaskChecklist> findByTask(Task task);

    Optional<TaskChecklist> findById(Integer id);

}

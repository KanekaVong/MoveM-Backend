package com.movem.backend.Repository.TaskRepositories;

import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Tasks.TaskChecklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskChecklistRepository
        extends JpaRepository<TaskChecklist, Integer> {

    List<TaskChecklist> findByTask(Task task);

    Optional<TaskChecklist> findById(Integer id);

    List<TaskChecklist> findByTaskOrderByIdAsc(Task task);

}

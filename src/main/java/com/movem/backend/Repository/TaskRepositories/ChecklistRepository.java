package com.movem.backend.Repository.TaskRepositories;

import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Tasks.Checklist;
import com.movem.backend.Entity.Trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {
    List<Checklist> findByTask(Task task);
    Optional<Checklist> findById(Integer id);
    List<Checklist> findByTaskOrderByIdAsc(Task task);
    List<Checklist> findByTripOrderByIdAsc(Trip trip);
}

package com.movem.backend.shared.checklist.repository;

import com.movem.backend.task.entities.Task;
import com.movem.backend.shared.checklist.entities.Checklist;
import com.movem.backend.trip.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {
    List<Checklist> findByTask(Task task);
    Optional<Checklist> findById(Integer id);
    List<Checklist> findByTaskOrderByIdAsc(Task task);
    List<Checklist> findByTripOrderByIdAsc(Trip trip);
}

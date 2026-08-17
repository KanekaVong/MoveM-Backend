package com.movem.backend.Repository.TaskRepositories;

import com.movem.backend.Entity.Tasks.TaskLabel;
import com.movem.backend.Entity.Auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskLabelRepository extends JpaRepository<TaskLabel, Integer> {

    List<TaskLabel> findByUser(User user);

    List<TaskLabel> findByIdInAndUser(List<Integer> ids, User user);

    Optional<TaskLabel> findByIdAndUser(Integer id, User user);

    Optional<TaskLabel> findByUserAndName(User user, String name);

    boolean existsByUserAndName(User user, String name);
}

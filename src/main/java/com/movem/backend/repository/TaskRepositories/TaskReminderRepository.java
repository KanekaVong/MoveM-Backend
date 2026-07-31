package com.movem.backend.repository.TaskRepositories;

import com.movem.backend.entity.Tasks.Task;
import com.movem.backend.entity.Tasks.TaskReminder;
import com.movem.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskReminderRepository
        extends JpaRepository<TaskReminder,Integer> {

    List<TaskReminder> findAllByTaskActivityUser(User user);

    List<TaskReminder> findByTaskActivityUserAndRemindAtAfterOrderByRemindAtAsc(
            User user,
            LocalDateTime now
    );

    @Query("""
SELECT tr
FROM TaskReminder tr
JOIN tr.task t
JOIN t.activity a
WHERE a.user = :user
AND tr.remindAt >= :start
AND tr.remindAt < :end
ORDER BY tr.remindAt
""")
    List<TaskReminder>
    findByTaskActivityUserAndRemindAtAfterAndRemindAtBeforeOrderByRemindAtAsc(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );

    List<TaskReminder> findByTask(Task task);
}
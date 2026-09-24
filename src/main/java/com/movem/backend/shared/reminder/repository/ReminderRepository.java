package com.movem.backend.shared.reminder.repository;

import com.movem.backend.task.entities.Task;
import com.movem.backend.shared.reminder.entities.Reminder;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.enums.shared.ReminderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Integer> {

    List<Reminder> findAllByTaskActivityUser(User user);
    List<Reminder> findByTaskActivityUserAndRemindAtAfterOrderByRemindAtAsc(User user, LocalDateTime now);
    @Query("""
        SELECT r
        FROM Reminder r
        JOIN r.task t
        JOIN t.activity a
        WHERE a.user = :user
        AND r.remindAt >= :start
        AND r.remindAt < :end
        ORDER BY r.remindAt
        """)
    List<Reminder> findByTaskActivityUserAndRemindAtAfterAndRemindAtBeforeOrderByRemindAtAsc(User user, LocalDateTime start, LocalDateTime end);
    List<Reminder> findByRemindAtLessThanEqualAndIsSentFalse(LocalDateTime now);
    List<Reminder> findByTask(Task task);
    List<Reminder> findByTaskOrderByRemindAtAsc(Task task);

    List<Reminder> findByTrip(Trip trip);
    Optional<Reminder> findByTripAndType(Trip trip, ReminderType type);
    void deleteByTrip(Trip trip);
}
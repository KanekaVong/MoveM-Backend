package com.movem.backend.scheduler;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.repository.SharedRepository.ActivityRepository;
import com.movem.backend.service.SharedServices.ActivityDeletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityCleanupScheduler {

    @Value("${app.activity.retention-days}")
    private int retentionDays;

    private final ActivityRepository activityRepository;
    private final ActivityDeletionService activityDeletionService;


    @Scheduled(cron = "0 45 8 * * ?")
    @Transactional
    public void cleanupDeletedActivities() {

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(retentionDays);

        List<Activity> expiredActivities =
                activityRepository.findByDeletedAtIsNotNullAndDeletedAtBefore(cutoff);

        if (expiredActivities.isEmpty()) {

            log.info("Activity cleanup finished. No expired activities found.");

            return;
        }

        log.info("Activity cleanup started. {} expired activities found.",
                expiredActivities.size());

        int success = 0;
        int failed = 0;

        for (Activity activity : expiredActivities) {

            try {

                activityDeletionService.permanentlyDelete(activity);

                success++;

                log.info("Permanently deleted activity [{}] {}",
                        activity.getId(),
                        activity.getActivityName());

            } catch (Exception ex) {
                failed++;
                ex.printStackTrace();
            }

        }

        log.info("""
                Activity cleanup completed.
                Deleted: {}
                Failed : {}
                """,
                success,
                failed
        );
    }
}
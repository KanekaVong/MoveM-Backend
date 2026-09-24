package com.movem.backend.shared.activity.services.impl;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.social.comment.repository.CommentRepository;
import com.movem.backend.shared.group.repositories.GroupRepository;
import com.movem.backend.shared.historyandlogs.activityfeed.repositories.ActivityFeedRepository;
import com.movem.backend.shared.activity.repositories.ActivityRepository;
import com.movem.backend.shared.historyandlogs.auditlog.repositories.AuditLogRepository;
import com.movem.backend.shared.activity.services.ActivityDeletionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityDeletionServiceImpl
        implements ActivityDeletionService {

    private final CommentRepository commentRepository;
    private final ActivityFeedRepository activityFeedRepository;
    private final GroupRepository groupRepository;
    private final ActivityRepository activityRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    public void permanentlyDelete(Activity activity) {

        activity.getLabels().clear();
        activityRepository.save(activity);

        for (Activity child : activity.getChildActivities()) {
            child.setParentActivity(null);
        }
        activityRepository.saveAll(activity.getChildActivities());

        commentRepository.deleteByActivity(activity);
        activityFeedRepository.deleteByActivity(activity);
        groupRepository.deleteByActivity(activity);

        int rows = auditLogRepository.detachActivity(activity.getId());

        System.out.println("Detached audit logs = " + rows);

        activityRepository.delete(activity);
    }
}

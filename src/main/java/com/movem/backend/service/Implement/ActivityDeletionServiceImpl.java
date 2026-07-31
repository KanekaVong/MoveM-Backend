package com.movem.backend.service.Implement;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.repository.CommentRepository.CommentRepository;
import com.movem.backend.repository.GroupRepository.GroupRepository;
import com.movem.backend.repository.SharedRepository.ActivityFeedRepository;
import com.movem.backend.repository.SharedRepository.ActivityRepository;
import com.movem.backend.repository.SharedRepository.AuditLogRepository;
import com.movem.backend.service.SharedServices.ActivityDeletionService;
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

        // Remove many-to-many labels
        activity.getLabels().clear();
        activityRepository.save(activity);

        // Remove parent references
        for (Activity child : activity.getChildActivities()) {
            child.setParentActivity(null);
        }
        activityRepository.saveAll(activity.getChildActivities());

        // Delete child records
        commentRepository.deleteByActivity(activity);
        activityFeedRepository.deleteByActivity(activity);
        groupRepository.deleteByActivity(activity);

        // IMPORTANT: detach audit logs BEFORE deleting activity

        int rows = auditLogRepository.detachActivity(activity.getId());

        System.out.println("Detached audit logs = " + rows);

        // Finally delete activity
        activityRepository.delete(activity);
    }
}

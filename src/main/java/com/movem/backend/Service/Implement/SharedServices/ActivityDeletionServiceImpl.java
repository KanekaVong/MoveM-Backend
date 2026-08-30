package com.movem.backend.Service.Implement.SharedServices;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Repository.SocialRepository.CommentRepository;
import com.movem.backend.Repository.CollaborationRepository.GroupRepository;
import com.movem.backend.Repository.SharedRepository.ActivityFeedRepository;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Repository.SharedRepository.AuditLogRepository;
import com.movem.backend.Service.SharedServices.ActivityDeletionService;
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

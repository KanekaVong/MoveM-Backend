package com.movem.backend.Service.SharedServices;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Collaboration.ActivityGroup;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.UnauthorizedActionException;
import com.movem.backend.Repository.CollaborationRepository.GroupRepository;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutSessionRepository;
import com.movem.backend.Repository.FriendRepository.FriendRepository;
import com.movem.backend.Repository.SharedRepository.GroupMemberRepository;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.model.enums.Collaboration.GroupRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityPermissionService {

    private final GroupRepository groupRepository;
    private final FriendRepository friendRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final FitnessWorkoutSessionRepository workoutSessionRepository;

    public void validateActivityAccess(
            Activity activity,
            User currentUser
    ) {

        if (activity.getUser().getId().equals(currentUser.getId())) {
            return;
        }

        ActivityGroup activityGroup =
                groupRepository.findByActivity(activity).orElse(null);

        if (activityGroup != null) {
            boolean isMember =
                    groupMemberRepository.existsByActivityGroupAndUser(
                            activityGroup,
                            currentUser
                    );

            if (isMember) {
                return;
            }
        }

        if (isSharedFitnessWorkout(activity, currentUser)) {
            return;
        }

        throw new UnauthorizedActionException(
                "You do not have access to this activity."
        );
    }

    public void validateActivityOwner(
            Activity activity,
            User currentUser
    ) {
        if (!activity.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException(
                    "Only the activity owner can perform this action."
            );
        }
    }

    public void validateGroupMember(
            Activity activity,
            User currentUser
    ) {
        validateActivityAccess(activity, currentUser);
    }

    public void validateCanEditActivity(
            Activity activity,
            User currentUser
    ) {
        validateActivityAccess(activity, currentUser);
    }

    public void validateCanManageTrip(
            Activity activity,
            User currentUser
    ) {
        validateActivityOwner(activity, currentUser);
    }

    public void validateCanContributeToTrip(
            Activity activity,
            User currentUser
    ) {
        validateActivityAccess(activity, currentUser);
    }

    public void validateGroupLeader(
            Activity activity,
            User currentUser
    ) {

        ActivityGroup activityGroup =
                groupRepository.findByActivity(activity)
                        .orElseThrow(() ->
                                new UnauthorizedActionException(
                                        "This activity is not a group."
                                ));

        boolean isLeader =
                groupMemberRepository.existsByActivityGroupAndUserAndRole(
                        activityGroup,
                        currentUser,
                        GroupRole.LEADER
                );

        if (!isLeader) {
            throw new UnauthorizedActionException(
                    "Only the group leader can perform this action."
            );
        }
    }

    public void validateCommentOwner(
            User commentOwner,
            User currentUser
    ) {
        if (!commentOwner.getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException(
                    "You can only modify your own comment."
            );
        }
    }

    private boolean isSharedFitnessWorkout(
            Activity activity,
            User currentUser
    ) {

        if (activity.getActivityType() != ActivityType.FITNESS) {
            return false;
        }

        if (activity.getStatus() != ActivityStatus.COMPLETE) {
            return false;
        }

        boolean isWorkout =
                workoutSessionRepository
                        .findByActivity(activity)
                        .isPresent();

        if (!isWorkout) {
            return false;
        }

        return areFriends(
                activity.getUser(),
                currentUser
        );
    }

    private boolean areFriends(
            User firstUser,
            User secondUser
    ) {

        User first =
                firstUser.getId() < secondUser.getId()
                        ? firstUser
                        : secondUser;

        User second =
                firstUser.getId() < secondUser.getId()
                        ? secondUser
                        : firstUser;

        return friendRepository.existsByUserOneAndUserTwo(
                first,
                second
        );
    }
}
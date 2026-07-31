package com.movem.backend.service.Implement.GroupServices;

import com.movem.backend.dto.request.GroupRequest.InviteMemberRequest;
import com.movem.backend.dto.request.GroupRequest.RequestToJoinRequest;
import com.movem.backend.dto.response.GroupResponse.*;
import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Group.*;
import com.movem.backend.entity.User;
import com.movem.backend.exception.DuplicateResourceException;
import com.movem.backend.exception.ResourceNotFoundException;
import com.movem.backend.exception.UnauthorizedActionException;
import com.movem.backend.mapper.GroupMapper.GroupMapper;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Group.GroupRole;
import com.movem.backend.model.enums.Group.InviteStatus;
import com.movem.backend.model.enums.Group.JoinRequestStatus;
import com.movem.backend.repository.SharedRepository.ActivityRepository;
import com.movem.backend.repository.AuthRepository.UserRepository;
import com.movem.backend.repository.GroupRepository.GroupInviteRepository;
import com.movem.backend.repository.GroupRepository.GroupMemberRepository;
import com.movem.backend.repository.GroupRepository.GroupRepository;
import com.movem.backend.repository.GroupRepository.JoinRequestRepository;
import com.movem.backend.service.AuthServices.CurrentUserService;
import com.movem.backend.service.SharedServices.ActivityFeedService;
import com.movem.backend.service.SharedServices.AuditLogService;
import com.movem.backend.service.SocialServices.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final GroupInviteRepository groupInviteRepository;

    private final JoinRequestRepository joinRequestRepository;

    private final ActivityRepository activityRepository;

    private final UserRepository userRepository;

    private final CurrentUserService currentUserService;

    private final ActivityFeedService activityFeedService;

    private final AuditLogService auditLogService;

    private final GroupMapper groupMapper;

    private ActivityGroup getOrCreateGroup(Activity activity) {

        return groupRepository
                .findByActivity(activity)
                .orElseGet(() -> {

                    ActivityGroup activityGroup = new ActivityGroup();

                    activityGroup.setActivity(activity);

                    activityGroup.setCreatedBy(activity.getUser());

                    activityGroup.setCreatedAt(LocalDateTime.now());

                    ActivityGroup savedActivityGroup =
                            groupRepository.save(activityGroup);

                    GroupMember member =
                            new GroupMember();

                    GroupMemberId id =
                            new GroupMemberId();

                    id.setGroupId(savedActivityGroup.getId());

                    id.setUserId(activity.getUser().getId());

                    member.setId(id);

                    member.setActivityGroup(savedActivityGroup);

                    member.setUser(activity.getUser());

                    member.setRole(GroupRole.LEADER);

                    member.setJoinedAt(LocalDateTime.now());

                    groupMemberRepository.save(member);

                    activityFeedService.createFeed(
                            activity,
                            activity.getUser(),
                            ActivityFeedEvent.GROUP_CREATED,
                            "created the group.",
                            null
                    );

                    return savedActivityGroup;

                });

    }

    @Override
    public GroupInviteResponse inviteMember(
            String activityId,
            InviteMemberRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Activity activity =
                getActivity(activityId);

        ActivityGroup activityGroup =
                getOrCreateGroup(activity);

        validateLeader(activityGroup, currentUser);

        User invitee =
                findUser(request.getIdentifier());

        if (invitee.getId().equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You cannot invite yourself."
            );

        }

        if (groupMemberRepository.existsByActivityGroupAndUser(
                activityGroup,
                invitee
        )) {

            throw new DuplicateResourceException(
                    "User is already a member."
            );

        }

        if (groupInviteRepository
                .findByActivityGroupAndInviteeAndStatus(
                        activityGroup,
                        invitee,
                        InviteStatus.PENDING
                )
                .isPresent()) {

            throw new DuplicateResourceException(
                    "A pending invitation already exists."
            );

        }

        GroupInvite invite =
                new GroupInvite();

        invite.setActivityGroup(activityGroup);

        invite.setInviter(currentUser);

        invite.setInvitee(invitee);

        invite.setStatus(InviteStatus.PENDING);

        invite.setInvitedAt(LocalDateTime.now());

        GroupInvite saved =
                groupInviteRepository.save(invite);

        activityFeedService.createFeed(
                activityGroup.getActivity(),
                currentUser,
                ActivityFeedEvent.MEMBER_INVITED,
                "invited " + invitee.getUsername() + ".",
                saved.getId()
        );

        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.MEMBER_INVITED,
                AuditCategory.GROUP,
                AuditSeverity.INFO,
                "member",
                "Invited member.",
                null,
                invitee.getUsername()
        );

        return groupMapper
                .toInviteResponse(saved);

    }

    @Override
    public GroupInviteResponse acceptInvite(Long inviteId) {

        User currentUser = currentUserService.getCurrentUser();

        GroupInvite invite = groupInviteRepository
                .findById(inviteId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invitation not found."
                        ));

        // Only the invited user can accept
        if (!invite.getInvitee().getId().equals(currentUser.getId())) {

            throw new UnauthorizedActionException(
                    "You cannot accept this invitation."
            );

        }

        // Already handled?
        if (invite.getStatus() != InviteStatus.PENDING) {

            throw new IllegalArgumentException(
                    "This invitation has already been processed."
            );

        }

        // Already a member?
        if (groupMemberRepository.existsByActivityGroupAndUser(
                invite.getActivityGroup(),
                currentUser
        )) {

            throw new DuplicateResourceException(
                    "You are already a member of this group."
            );

        }

        GroupMember member = new GroupMember();

        GroupMemberId id = new GroupMemberId();

        id.setGroupId(invite.getActivityGroup().getId());

        id.setUserId(currentUser.getId());

        member.setId(id);

        member.setActivityGroup(invite.getActivityGroup());

        member.setUser(currentUser);

        member.setRole(GroupRole.MEMBER);

        member.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(member);

        activityFeedService.createFeed(
                invite.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.MEMBER_JOINED,
                "joined the group.",
                null
        );

        auditLogService.createLog(
                invite.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.MEMBER_JOINED,
                AuditCategory.GROUP,
                AuditSeverity.INFO,
                "member",
                "Joined group.",
                null,
                currentUser.getUsername()
        );

        invite.setStatus(InviteStatus.ACCEPTED);

        invite.setRespondedAt(LocalDateTime.now());

        GroupInvite saved =
                groupInviteRepository.save(invite);

        activityFeedService.createFeed(
                invite.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.INVITE_ACCEPTED,
                "accepted the invitation.",
                invite.getId()
        );

        auditLogService.createLog(
                invite.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.INVITE_ACCEPTED,
                AuditCategory.GROUP,
                AuditSeverity.INFO,
                "status",
                "Accepted invitation.",
                "PENDING",
                "ACCEPTED"
        );

        return groupMapper.toInviteResponse(saved);

    }

    @Override
    public GroupInviteResponse rejectInvite(Long inviteId) {

        User currentUser = currentUserService.getCurrentUser();

        GroupInvite invite = groupInviteRepository
                .findById(inviteId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invitation not found."
                        ));

        if (!invite.getInvitee().getId().equals(currentUser.getId())) {

            throw new UnauthorizedActionException(
                    "You cannot reject this invitation."
            );

        }

        if (invite.getStatus() != InviteStatus.PENDING) {

            throw new IllegalArgumentException(
                    "This invitation has already been processed."
            );

        }

        invite.setStatus(InviteStatus.REJECTED);

        invite.setRespondedAt(LocalDateTime.now());

        GroupInvite saved =
                groupInviteRepository.save(invite);

        activityFeedService.createFeed(
                invite.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.INVITE_REJECTED,
                "rejected the invitation.",
                invite.getId()
        );

        auditLogService.createLog(
                invite.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.INVITE_REJECTED,
                AuditCategory.GROUP,
                AuditSeverity.INFO,
                "status",
                "Rejected invitation.",
                "PENDING",
                "REJECTED"
        );

        return groupMapper.toInviteResponse(saved);

    }

    @Override
    public JoinRequestResponse requestToJoin(
            RequestToJoinRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();

        ActivityGroup group = groupRepository
                .findByJoinToken(request.getJoinToken())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid join link."
                        ));

        // Already a member?
        if (groupMemberRepository.existsByActivityGroupAndUser(
                group,
                currentUser
        )) {

            throw new DuplicateResourceException(
                    "You are already a member of this group."
            );

        }

        // Already requested?
        if (joinRequestRepository
                .findByActivityGroupAndRequester(
                        group,
                        currentUser
                )
                .isPresent()) {

            throw new DuplicateResourceException(
                    "You already have a pending request."
            );

        }

        JoinRequest joinRequest = new JoinRequest();

        joinRequest.setActivityGroup(group);

        joinRequest.setRequester(currentUser);

        joinRequest.setStatus(
                JoinRequestStatus.PENDING
        );

        joinRequest.setRequestedAt(
                LocalDateTime.now()
        );

        JoinRequest saved =
                joinRequestRepository.save(joinRequest);

        activityFeedService.createFeed(
                joinRequest.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.JOIN_REQUEST_SENT,
                "requested to join the activity.",
                saved.getId()
        );

        auditLogService.createLog(
                joinRequest.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.JOIN_REQUEST_SENT,
                AuditCategory.GROUP,
                AuditSeverity.INFO,
                "status",
                "Requested to join.",
                null,
                "PENDING"
        );

        return groupMapper.toJoinRequestResponse(saved);

    }

    @Override
    public JoinRequestResponse approveJoinRequest(
            Long requestId
    ) {

        User currentUser = currentUserService.getCurrentUser();

        JoinRequest joinRequest = joinRequestRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Join request not found."
                        ));

        ActivityGroup group =
                joinRequest.getActivityGroup();

        validateLeader(group, currentUser);

        if (joinRequest.getStatus() != JoinRequestStatus.PENDING) {

            throw new IllegalArgumentException(
                    "This request has already been processed."
            );

        }

        if (!groupMemberRepository.existsByActivityGroupAndUser(
                group,
                joinRequest.getRequester()
        )) {

            GroupMember member =
                    new GroupMember();

            GroupMemberId id =
                    new GroupMemberId();

            id.setGroupId(group.getId());

            id.setUserId(
                    joinRequest.getRequester().getId()
            );

            member.setId(id);

            member.setActivityGroup(group);

            member.setUser(
                    joinRequest.getRequester()
            );

            member.setRole(GroupRole.MEMBER);

            member.setJoinedAt(LocalDateTime.now());

            groupMemberRepository.save(member);

        }

        joinRequest.setStatus(
                JoinRequestStatus.APPROVED
        );

        joinRequest.setRespondedAt(
                LocalDateTime.now()
        );

        JoinRequest saved =
                joinRequestRepository.save(joinRequest);

        activityFeedService.createFeed(
                joinRequest.getActivityGroup().getActivity(),
                joinRequest.getRequester(),
                ActivityFeedEvent.JOIN_REQUEST_APPROVED,
                "joined the activity.",
                joinRequest.getId()
        );

        auditLogService.createLog(
                joinRequest.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.JOIN_REQUEST_APPROVED,
                AuditCategory.GROUP,
                AuditSeverity.INFO,
                "status",
                "Approved join request.",
                "PENDING",
                "APPROVED"
        );

        return groupMapper.toJoinRequestResponse(saved);

    }

    @Override
    public JoinRequestResponse rejectJoinRequest(
            Long requestId
    ) {

        User currentUser = currentUserService.getCurrentUser();

        JoinRequest joinRequest = joinRequestRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Join request not found."
                        ));

        ActivityGroup group =
                joinRequest.getActivityGroup();

        validateLeader(group, currentUser);

        if (joinRequest.getStatus() != JoinRequestStatus.PENDING) {

            throw new IllegalArgumentException(
                    "This request has already been processed."
            );

        }

        joinRequest.setStatus(
                JoinRequestStatus.REJECTED
        );

        joinRequest.setRespondedAt(
                LocalDateTime.now()
        );

        JoinRequest saved =
                joinRequestRepository.save(joinRequest);

        activityFeedService.createFeed(
                joinRequest.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.JOIN_REQUEST_REJECTED,
                "rejected a join request.",
                joinRequest.getId()
        );

        auditLogService.createLog(
                joinRequest.getActivityGroup().getActivity(),
                currentUser,
                ActivityFeedEvent.JOIN_REQUEST_REJECTED,
                AuditCategory.GROUP,
                AuditSeverity.INFO,
                "member",
                "Rejected join request.",
                null,
                joinRequest.getRequester().getUsername()
        );

        return groupMapper.toJoinRequestResponse(saved);

    }

    @Override
    @Transactional
    public void leaveGroup(String activityId) {

        User currentUser = currentUserService.getCurrentUser();

        Activity activity =
                activityRepository.findById(activityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Activity not found."
                                ));

        ActivityGroup group =
                groupRepository.findByActivity(activity)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group not found."
                                ));

        GroupMember member =
                groupMemberRepository
                        .findByActivityGroupAndUser(
                                group,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "You are not a member of this group."
                                ));

        // Leader cannot leave while still the leader
        if (member.getRole() == GroupRole.LEADER) {
            throw new IllegalArgumentException(
                    "You're not supposed to leave your own group."
            );
        }

        groupMemberRepository.delete(member);

        activityFeedService.createFeed(
                activity,
                currentUser,
                ActivityFeedEvent.MEMBER_LEFT,
                "left the activity.",
                null
        );

        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.MEMBER_LEFT,
                AuditCategory.GROUP,
                AuditSeverity.WARNING,
                "member",
                "Left group.",
                currentUser.getUsername(),
                null
        );

    }

    @Override
    @Transactional
    public void removeMember(
            String activityId,
            Integer memberId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Activity activity =
                activityRepository.findById(activityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Activity not found."
                                ));

        // Only activity owner can remove members
        if (!activity.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException(
                    "Only the activity owner can remove members."
            );
        }

        ActivityGroup activityGroup =
                groupRepository.findByActivity(activity)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group not found."
                                ));

        User member =
                userRepository.findById(memberId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found."
                                ));

        GroupMember groupMember =
                groupMemberRepository
                        .findByActivityGroupAndUser(
                                activityGroup,
                                member
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Member not found."
                                ));

        // Prevent removing yourself
        if (member.getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException(
                    "Use Leave Group instead."
            );
        }

        groupMemberRepository.delete(groupMember);

        activityFeedService.createFeed(
                activity,
                currentUser,
                ActivityFeedEvent.MEMBER_REMOVED,
                "removed " + member.getUsername() + ".",
                null
        );

        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.MEMBER_REMOVED,
                AuditCategory.GROUP,
                AuditSeverity.WARNING,
                "member",
                "Removed member.",
                member.getUsername(),
                null
        );

    }

    private User findUser(String identifier) {

        if (identifier.contains("@")) {

            return userRepository
                    .findByEmail(identifier)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "User not found."
                            ));

        }

        return userRepository
                .findByUsername(identifier)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."
                        ));

    }

    private void validateLeader(
            ActivityGroup activityGroup,
            User currentUser
    ) {

        GroupMember leader =
                groupMemberRepository
                        .findByActivityGroupAndUser(
                                activityGroup,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new UnauthorizedActionException(
                                        "You are not a member of this group."
                                ));

        if (leader.getRole() != GroupRole.LEADER) {
            throw new UnauthorizedActionException(
                    "Only the leader can perform this action."
            );
        }

    }

    @Override
    public List<GroupMemberResponse> getMembers(
            String activityId
    ) {

        Activity activity = getActivity(activityId);

        ActivityGroup group = groupRepository
                .findByActivity(activity)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Group not found."
                        ));

        return groupMemberRepository
                .findByActivityGroup(group)
                .stream()
                .map(groupMapper::toMemberResponse)
                .toList();

    }

    @Override
    public List<GroupInviteResponse> getMyInvitations() {

        User currentUser = currentUserService.getCurrentUser();

        return groupInviteRepository
                .findByInviteeAndStatusOrderByInvitedAtDesc(
                        currentUser,
                        InviteStatus.PENDING
                )
                .stream()
                .map(groupMapper::toInviteResponse)
                .toList();
    }

    @Override
    public List<PendingInviteResponse> getPendingInvites(
            String activityId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Activity activity =
                getActivity(activityId);

        ActivityGroup group =
                groupRepository.findByActivity(activity)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group not found."
                                ));

        validateLeader(group, currentUser);

        return groupInviteRepository
                .findByActivityGroupAndStatus(
                        group,
                        InviteStatus.PENDING
                )
                .stream()
                .map(groupMapper::toPendingInviteResponse)
                .toList();

    }

    @Override
    public JoinLinkResponse generateJoinLink(
            String activityId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Activity activity =
                getActivity(activityId);

        if (!activity.getUser().getId().equals(currentUser.getId())) {

            throw new UnauthorizedActionException(
                    "Only the owner can generate a join link."
            );

        }

        ActivityGroup group =
                getOrCreateGroup(activity);

        if (group.getJoinToken() == null
                || group.getJoinToken().isBlank()) {

            group.setJoinToken(
                    UUID.randomUUID().toString()
            );

            groupRepository.save(group);

        }

        return groupMapper.toJoinLinkResponse(group);

    }

    @Override
    public JoinLinkResponse getJoinLink(
            String activityId
    ) {

        User currentUser = currentUserService.getCurrentUser();

        Activity activity = getActivity(activityId);

        if (!activity.getUser().getId().equals(currentUser.getId())) {

            throw new UnauthorizedActionException(
                    "Only the owner can view the join link."
            );

        }

        ActivityGroup group = groupRepository
                .findByActivity(activity)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Group not found."
                        ));

        if (group.getJoinToken() == null
                || group.getJoinToken().isBlank()) {

            throw new ResourceNotFoundException(
                    "Join link has not been generated yet."
            );

        }

        return groupMapper.toJoinLinkResponse(group);

    }

    @Override
    public List<JoinRequestResponse> getPendingJoinRequests(
            String activityId
    ) {

        User currentUser = currentUserService.getCurrentUser();

        Activity activity = getActivity(activityId);

        ActivityGroup group = groupRepository
                .findByActivity(activity)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Group not found."
                        ));

        validateLeader(group, currentUser);

        return joinRequestRepository
                .findByActivityGroupAndStatus(
                        group,
                        JoinRequestStatus.PENDING
                )
                .stream()
                .map(groupMapper::toJoinRequestResponse)
                .toList();

    }

    @Override
    public List<GroupSearchUserResponse> searchUsers(
            String keyword
    ) {

        keyword = keyword.trim().toLowerCase();

        // Minimum 3 characters
        if (keyword.length() < 3) {
            return List.of();
        }

        // Don't allow searching by only email domain
        if (keyword.startsWith("@")) {
            return List.of();
        }

        // Don't allow numbers-only searches
        if (keyword.matches("\\d+")) {
            return List.of();
        }

        User currentUser =
                currentUserService.getCurrentUser();

        return userRepository
                .searchUsers(
                        keyword,
                        PageRequest.of(0, 20)
                )
                .stream()

                // Don't show yourself
                .filter(user ->
                        !user.getId().equals(currentUser.getId())
                )

                .map(groupMapper::toGroupSearchUserResponse)

                .toList();
    }

    @Override
    public List<MyGroupResponse> getMyGroups() {

        User currentUser =
                currentUserService.getCurrentUser();

        List<GroupMember> memberships =
                groupMemberRepository.findByUser(currentUser);

        return memberships.stream()
                .map(member -> {

                    ActivityGroup activityGroup =
                            member.getActivityGroup();

                    Activity activity =
                            activityGroup.getActivity();

                    MyGroupResponse response =
                            new MyGroupResponse();

                    response.setGroupId(
                            activityGroup.getId()
                    );

                    response.setActivityId(
                            activity.getId()
                    );

                    response.setActivityName(
                            activity.getActivityName()
                    );

                    response.setActivityDescription(
                            activity.getDescription()
                    );

                    response.setCreatedAt(
                            activityGroup.getCreatedAt()
                    );

                    response.setRole(
                            member.getRole()
                    );

                    response.setMemberCount(
                            (int) groupMemberRepository
                                    .countByActivityGroup(activityGroup)
                    );

                    return response;

                })
                .toList();

    }

    private Activity getActivity(String activityId) {

        return activityRepository
                .findById(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Activity not found."
                        ));

    }

}

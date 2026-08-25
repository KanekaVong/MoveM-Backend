package com.movem.backend.Service.Implement.GroupServices;

import com.movem.backend.Dto.request.GroupAndCollabRequest.InviteMemberRequest;
import com.movem.backend.Dto.request.GroupAndCollabRequest.RequestToJoinRequest;
import com.movem.backend.Dto.response.GroupAndCollabResponse.*;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Collaboration.ActivityGroup;
import com.movem.backend.Entity.Collaboration.GroupInvite;
import com.movem.backend.Entity.Shared.GroupMember;
import com.movem.backend.Entity.Shared.GroupMemberId;
import com.movem.backend.Entity.Shared.JoinRequest;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.DuplicateResourceException;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Exception.UnauthorizedActionException;
import com.movem.backend.Mapper.CollaborationMapper.GroupMapper;
import com.movem.backend.Service.Event.Factory.GroupEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.model.enums.Collaboration.GroupRole;
import com.movem.backend.model.enums.Collaboration.InviteStatus;
import com.movem.backend.model.enums.Collaboration.JoinRequestStatus;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Repository.AuthRepository.UserRepository;
import com.movem.backend.Repository.CollaborationRepository.GroupInviteRepository;
import com.movem.backend.Repository.SharedRepository.GroupMemberRepository;
import com.movem.backend.Repository.CollaborationRepository.GroupRepository;
import com.movem.backend.Repository.SharedRepository.JoinRequestRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.CollaborationService.GroupService;
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
    private final FeatureEventTrackingService featureEventTrackingService;
    private final GroupEventFactory groupEventFactory;
    private final GroupMapper groupMapper;

    @Override
    public ActivityGroup getOrCreateGroup(Activity activity) {

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

                    featureEventTrackingService.handle(
                            groupEventFactory.groupCreated(
                                    activity,
                                    activity.getUser()
                            )
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

        if (!activity.getUser().getId().equals(
                currentUser.getId()
        )) {

            throw new UnauthorizedActionException(
                    "Only the Trip owner can invite members."
            );
        }

        ActivityGroup activityGroup =
                getOrCreateGroup(activity);

        User invitee =
                findUser(request.getIdentifier());

        if (invitee.getId().equals(
                currentUser.getId()
        )) {

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

        invite.setActivityGroup(
                activityGroup
        );

        invite.setInviter(
                currentUser
        );

        invite.setInvitee(
                invitee
        );

        invite.setStatus(
                InviteStatus.PENDING
        );

        invite.setInvitedAt(
                LocalDateTime.now()
        );

        GroupInvite saved =
                groupInviteRepository.save(invite);

        featureEventTrackingService.handle(
                groupEventFactory.memberInvited(
                        activityGroup.getActivity(),
                        currentUser,
                        invitee,
                        saved.getId()
                )
        );

        return groupMapper
                .toInviteResponse(saved);
    }

    @Override
    public GroupInviteResponse acceptInvite(Long inviteId) {

        User currentUser =
                currentUserService.getCurrentUser();

        GroupInvite invite =
                groupInviteRepository
                        .findById(inviteId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Invitation not found."
                                ));

        if (!invite.getInvitee().getId().equals(
                currentUser.getId()
        )) {

            throw new UnauthorizedActionException(
                    "You cannot accept this invitation."
            );
        }

        if (invite.getStatus() != InviteStatus.PENDING) {

            throw new IllegalArgumentException(
                    "This invitation has already been processed."
            );
        }

        ActivityGroup group =
                invite.getActivityGroup();


        User owner =
                group.getCreatedBy();

        if (!groupMemberRepository.existsByActivityGroupAndUser(
                group,
                owner
        )) {

            GroupMember ownerMember =
                    new GroupMember();

            GroupMemberId ownerId =
                    new GroupMemberId();

            ownerId.setGroupId(
                    group.getId()
            );

            ownerId.setUserId(
                    owner.getId()
            );

            ownerMember.setId(ownerId);

            ownerMember.setActivityGroup(
                    group
            );

            ownerMember.setUser(
                    owner
            );

            ownerMember.setRole(
                    GroupRole.LEADER
            );

            ownerMember.setJoinedAt(
                    LocalDateTime.now()
            );

            groupMemberRepository.save(
                    ownerMember
            );
        }

        if (groupMemberRepository.existsByActivityGroupAndUser(
                group,
                currentUser
        )) {

            throw new DuplicateResourceException(
                    "You are already a member of this group."
            );
        }

        GroupMember member =
                new GroupMember();

        GroupMemberId memberId =
                new GroupMemberId();

        memberId.setGroupId(
                group.getId()
        );

        memberId.setUserId(
                currentUser.getId()
        );

        member.setId(memberId);

        member.setActivityGroup(
                group
        );

        member.setUser(
                currentUser
        );

        member.setRole(
                GroupRole.MEMBER
        );

        member.setJoinedAt(
                LocalDateTime.now()
        );

        groupMemberRepository.save(
                member
        );

        invite.setStatus(
                InviteStatus.ACCEPTED
        );

        invite.setRespondedAt(
                LocalDateTime.now()
        );

        GroupInvite saved =
                groupInviteRepository.save(invite);

        featureEventTrackingService.handle(
                groupEventFactory.memberJoined(
                        group.getActivity(),
                        currentUser
                )
        );

        featureEventTrackingService.handle(
                groupEventFactory.inviteAccepted(
                        invite.getActivityGroup().getActivity(),
                        currentUser,
                        invite.getId(),
                        invite.getInviter()
                )
        );

        return groupMapper.toInviteResponse(
                saved
        );
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
            );}

        if (invite.getStatus() != InviteStatus.PENDING) {

            throw new IllegalArgumentException(
                    "This invitation has already been processed."
            );}
        invite.setStatus(InviteStatus.REJECTED);
        invite.setRespondedAt(LocalDateTime.now());
        GroupInvite saved = groupInviteRepository.save(invite);
        featureEventTrackingService.handle(
                groupEventFactory.inviteRejected(
                        invite.getActivityGroup().getActivity(),
                        currentUser,
                        invite.getId(),
                        invite.getInviter()
                )
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
        if (groupMemberRepository.existsByActivityGroupAndUser(
                group,
                currentUser
        )) {

            throw new DuplicateResourceException(
                    "You are already a member of this group."
            );

        }

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

        featureEventTrackingService.handle(
                groupEventFactory.joinRequestSent(
                        group.getActivity(),
                        currentUser,
                        group,
                        saved.getId()
                )
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

        featureEventTrackingService.handle(
                groupEventFactory.joinRequestApproved(
                        group.getActivity(),
                        currentUser,
                        joinRequest
                )
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

        featureEventTrackingService.handle(
                groupEventFactory.joinRequestRejected(
                        group.getActivity(),
                        currentUser,
                        joinRequest
                )
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

        if (member.getRole() == GroupRole.LEADER) {
            throw new IllegalArgumentException(
                    "You're not supposed to leave your own group."
            );
        }

        groupMemberRepository.delete(member);

        featureEventTrackingService.handle(
                groupEventFactory.memberLeft(
                        activity,
                        currentUser,
                        group
                )
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

        if (member.getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException(
                    "Use Leave Group instead."
            );
        }

        groupMemberRepository.delete(groupMember);

        featureEventTrackingService.handle(
                groupEventFactory.memberRemoved(
                        activity,
                        currentUser,
                        member
                )
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

        if (keyword.length() < 3) {
            return List.of();
        }

        if (keyword.startsWith("@")) {
            return List.of();
        }

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

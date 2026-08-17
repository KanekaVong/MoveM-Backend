package com.movem.backend.Mapper.CollaborationMapper;

import com.movem.backend.Dto.response.GroupAndCollabResponse.*;
import com.movem.backend.Entity.Collaboration.ActivityGroup;
import com.movem.backend.Entity.Collaboration.GroupInvite;
import com.movem.backend.Entity.Shared.GroupMember;
import com.movem.backend.Entity.Shared.JoinRequest;
import com.movem.backend.Entity.Auth.User;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public GroupInviteResponse toInviteResponse(
            GroupInvite invite
    ) {

        GroupInviteResponse response =
                new GroupInviteResponse();

        response.setInviteId(invite.getId());

        response.setGroupId(
                invite.getActivityGroup().getId()
        );

        response.setActivityId(
                invite.getActivityGroup()
                        .getActivity()
                        .getId()
        );

        response.setActivityName(
                invite.getActivityGroup()
                        .getActivity()
                        .getActivityName()
        );

        response.setInviterId(
                invite.getInviter().getId()
        );

        response.setInviterUsername(
                invite.getInviter().getUsername()
        );

        response.setInviteeId(
                invite.getInvitee().getId()
        );

        response.setInviteeUsername(
                invite.getInvitee().getUsername()
        );

        response.setStatus(
                invite.getStatus()
        );

        response.setInvitedAt(
                invite.getInvitedAt()
        );

        response.setRespondedAt(
                invite.getRespondedAt()
        );

        return response;
    }

    public GroupMemberResponse toMemberResponse(
            GroupMember member
    ) {

        GroupMemberResponse response =
                new GroupMemberResponse();

        response.setUserId(
                member.getUser().getId()
        );

        response.setUsername(
                member.getUser().getUsername()
        );

        response.setFirstname(
                member.getUser().getFirstname()
        );

        response.setLastname(
                member.getUser().getLastname()
        );

        response.setProfilePic(
                member.getUser().getProfilePic()
        );

        response.setRole(
                member.getRole()
        );

        response.setJoinedAt(
                member.getJoinedAt()
        );

        return response;
    }

    public GroupMemberResponse toGroupMemberResponse(
            GroupMember member
    ) {

        GroupMemberResponse response =
                new GroupMemberResponse();

        response.setUserId(
                member.getUser().getId()
        );

        response.setUsername(
                member.getUser().getUsername()
        );

        response.setFirstname(
                member.getUser().getFirstname()
        );

        response.setLastname(
                member.getUser().getLastname()
        );

        response.setRole(
                member.getRole()
        );

        response.setJoinedAt(
                member.getJoinedAt()
        );

        return response;

    }

    public JoinRequestResponse toJoinRequestResponse(
            JoinRequest joinRequest
    ) {

        JoinRequestResponse response =
                new JoinRequestResponse();

        response.setRequestId(joinRequest.getId());

        response.setGroupId(
                joinRequest.getActivityGroup().getId()
        );

        response.setActivityId(
                joinRequest.getActivityGroup().getActivity().getId()
        );

        response.setActivityName(
                joinRequest.getActivityGroup().getActivity().getActivityName()
        );

        response.setRequesterId(
                joinRequest.getRequester().getId()
        );

        response.setRequesterUsername(
                joinRequest.getRequester().getUsername()
        );

        response.setRequestedAt(
                joinRequest.getRequestedAt()
        );

        response.setRespondedAt(
                joinRequest.getRespondedAt()
        );

        response.setStatus(
                joinRequest.getStatus()
        );

        return response;

    }

    public JoinLinkResponse toJoinLinkResponse(
            ActivityGroup group
    ) {

        JoinLinkResponse response =
                new JoinLinkResponse();

        response.setJoinToken(group.getJoinToken());

        response.setJoinLink(
                "https://movem.app/join/" +
                        group.getJoinToken()
        );

        return response;

    }

    public PendingInviteResponse toPendingInviteResponse(
            GroupInvite invite
    ) {

        PendingInviteResponse response =
                new PendingInviteResponse();

        response.setInviteId(invite.getId());

        response.setGroupId(
                invite.getActivityGroup().getId()
        );

        response.setActivityId(
                invite.getActivityGroup()
                        .getActivity()
                        .getId()
        );

        response.setActivityName(
                invite.getActivityGroup()
                        .getActivity()
                        .getActivityName()
        );

        response.setInviteeId(
                invite.getInvitee().getId()
        );

        response.setInviteeUsername(
                invite.getInvitee().getUsername()
        );

        response.setInviteeEmail(
                invite.getInvitee().getEmail()
        );

        response.setStatus(
                invite.getStatus()
        );

        response.setInvitedAt(
                invite.getInvitedAt()
        );

        return response;

    }
    public GroupSearchUserResponse toGroupSearchUserResponse(User user) {

        GroupSearchUserResponse response =
                new GroupSearchUserResponse();

        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setEmail(user.getEmail());

        return response;
    }

}
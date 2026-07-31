package com.movem.backend.service.SocialServices;

import com.movem.backend.dto.request.GroupRequest.InviteMemberRequest;
import com.movem.backend.dto.request.GroupRequest.RequestToJoinRequest;
import com.movem.backend.dto.response.GroupResponse.*;

import java.util.List;

public interface GroupService {

    GroupInviteResponse inviteMember(
            String activityId,
            InviteMemberRequest request
    );

    GroupInviteResponse acceptInvite(
            Long inviteId
    );

    GroupInviteResponse rejectInvite(
            Long inviteId
    );

    JoinRequestResponse requestToJoin(
            RequestToJoinRequest request
    );

    JoinRequestResponse approveJoinRequest(
            Long requestId
    );

    JoinRequestResponse rejectJoinRequest(
            Long requestId
    );

    void removeMember(
            String activityId,
            Integer memberId
    );

    List<GroupMemberResponse> getMembers(
            String activityId
    );

    List<GroupInviteResponse> getMyInvitations();

    List<JoinRequestResponse> getPendingJoinRequests(
            String activityId
    );

    JoinLinkResponse generateJoinLink(
            String activityId
    );

    JoinLinkResponse getJoinLink(String activityId);

    List<PendingInviteResponse> getPendingInvites(
            String activityId
    );

    List<GroupSearchUserResponse> searchUsers(
            String keyword
    );

    List<MyGroupResponse> getMyGroups();

    void leaveGroup(String activityId);

}
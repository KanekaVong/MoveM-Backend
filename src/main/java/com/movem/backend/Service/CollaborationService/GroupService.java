package com.movem.backend.Service.CollaborationService;

import com.movem.backend.Dto.request.GroupAndCollabRequest.InviteMemberRequest;
import com.movem.backend.Dto.request.GroupAndCollabRequest.RequestToJoinRequest;
import com.movem.backend.Dto.response.GroupAndCollabResponse.*;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Collaboration.ActivityGroup;

import java.util.List;

public interface GroupService {

    ActivityGroup getOrCreateGroup(Activity activity);

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
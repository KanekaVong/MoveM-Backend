package com.movem.backend.shared.group.services;

import com.movem.backend.shared.group.dtos.requests.InviteMemberRequest;
import com.movem.backend.shared.group.dtos.requests.RequestToJoinRequest;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.shared.group.dtos.responses.*;
import com.movem.backend.shared.group.entities.ActivityGroup;

import java.util.List;

public interface GroupService {

    ActivityGroup getOrCreateGroup(Activity activity);

    GroupInviteResponse inviteMember(String activityId, InviteMemberRequest request);
    GroupInviteResponse acceptInvite(Long inviteId);
    GroupInviteResponse rejectInvite(Long inviteId);

    JoinRequestResponse requestToJoin(RequestToJoinRequest request);
    JoinRequestResponse approveJoinRequest(Long requestId);
    JoinRequestResponse rejectJoinRequest(Long requestId);

    void removeMember(String activityId, Integer memberId);

    List<GroupMemberResponse> getMembers(String activityId);
    List<GroupInviteResponse> getMyInvitations();
    List<JoinRequestResponse> getPendingJoinRequests(String activityId);

    JoinLinkResponse generateJoinLink(String activityId);
    JoinLinkResponse getJoinLink(String activityId);

    List<PendingInviteResponse> getPendingInvites(String activityId);
    List<GroupSearchUserResponse> searchUsers(String keyword);
    List<MyGroupResponse> getMyGroups();

    void leaveGroup(String activityId);

}
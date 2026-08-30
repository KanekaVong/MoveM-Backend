package com.movem.backend.Controller.SocialController;

import com.movem.backend.Dto.request.GroupAndCollabRequest.InviteMemberRequest;
import com.movem.backend.Dto.request.GroupAndCollabRequest.RequestToJoinRequest;
import com.movem.backend.Dto.response.GroupAndCollabResponse.*;
import com.movem.backend.Service.CollaborationService.GroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@Tag(
        name = "Shared - Groups",
        description = "Used for Tasks with Collaborators"
)
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/{activityId}/invite")
    public GroupInviteResponse inviteMember(
            @PathVariable String activityId,
            @Valid @RequestBody InviteMemberRequest request
    ) {

        return groupService.inviteMember(
                activityId,
                request
        );

    }

    @PatchMapping("/invites/{inviteId}/accept")
    public GroupInviteResponse acceptInvite(
            @PathVariable Long inviteId
    ) {

        return groupService.acceptInvite(inviteId);

    }

    @PatchMapping("/invites/{inviteId}/reject")
    public GroupInviteResponse rejectInvite(
            @PathVariable Long inviteId
    ) {

        return groupService.rejectInvite(inviteId);

    }

    @GetMapping("/{activityId}/pending-invites")
    public ResponseEntity<List<PendingInviteResponse>>
    getPendingInvites(
            @PathVariable String activityId
    ) {

        return ResponseEntity.ok(
                groupService.getPendingInvites(activityId)
        );

    }

    @PostMapping("/join")
    public ResponseEntity<JoinRequestResponse> requestToJoin(
            @Valid
            @RequestBody RequestToJoinRequest request
    ) {

        return ResponseEntity.ok(
                groupService.requestToJoin(request)
        );

    }

    @PatchMapping("/join-requests/{requestId}/reject")
    public ResponseEntity<JoinRequestResponse>
    rejectJoinRequest(
            @PathVariable Long requestId
    ) {

        return ResponseEntity.ok(
                groupService.rejectJoinRequest(requestId)
        );

    }

    @GetMapping("/{activityId}/members")
    public List<GroupMemberResponse> getMembers(
            @PathVariable String activityId
    ) {

        return groupService.getMembers(activityId);

    }

    @GetMapping("/my-invitations")
    public List<GroupInviteResponse> getMyInvitations() {

        return groupService.getMyInvitations();

    }

    @GetMapping("/{activityId}/join-requests")
    public List<JoinRequestResponse> getPendingJoinRequests(
            @PathVariable String activityId
    ) {

        return groupService.getPendingJoinRequests(
                activityId
        );

    }

    @PostMapping("/{activityId}/join-link")
    public ResponseEntity<JoinLinkResponse> generateJoinLink(
            @PathVariable String activityId
    ) {

        return ResponseEntity.ok(
                groupService.generateJoinLink(activityId)
        );
    }

    @GetMapping("/{activityId}/join-link")
    public ResponseEntity<JoinLinkResponse> getJoinLink(
            @PathVariable String activityId
    ) {

        return ResponseEntity.ok(
                groupService.getJoinLink(activityId)
        );

    }

    @PatchMapping("/join-requests/{requestId}/approve")
    public ResponseEntity<JoinRequestResponse>
    approveJoinRequest(
            @PathVariable Long requestId
    ) {

        return ResponseEntity.ok(
                groupService.approveJoinRequest(requestId)
        );

    }

    @GetMapping("/search-users")
    public ResponseEntity<List<GroupSearchUserResponse>> searchUsers(
            @RequestParam String keyword
    ) {

        return ResponseEntity.ok(
                groupService.searchUsers(keyword)
        );

    }

    @DeleteMapping("/{activityId}/leave")
    public ResponseEntity<Void> leaveGroup(
            @PathVariable String activityId
    ) {

        groupService.leaveGroup(activityId);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{activityId}/members/{memberId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable String activityId,
            @PathVariable Integer memberId
    ) {

        groupService.removeMember(
                activityId,
                memberId
        );

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/my-groups")
    public ResponseEntity<List<MyGroupResponse>> getMyGroups() {

        return ResponseEntity.ok(
                groupService.getMyGroups()
        );

    }

}
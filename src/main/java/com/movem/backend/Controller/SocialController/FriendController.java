package com.movem.backend.Controller.SocialController;

import com.movem.backend.Dto.request.FriendRequest.SendFriendRequestRequest;
import com.movem.backend.Dto.response.FriendResponse.FriendRequestResponse;
import com.movem.backend.Dto.response.FriendResponse.FriendResponse;
import com.movem.backend.Dto.response.FriendResponse.SearchUserResponse;
import com.movem.backend.Service.FriendServices.FriendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // Send friend request
    @PostMapping("/request")
    public ResponseEntity<FriendRequestResponse> sendFriendRequest(
            @Valid @RequestBody SendFriendRequestRequest request
    ) {
        return ResponseEntity.ok(friendService.sendFriendRequest(request));
    }

    // Accept friend request
    @PatchMapping("/requests/{requestId}/accept")
    public ResponseEntity<FriendRequestResponse> acceptFriendRequest(
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(friendService.acceptFriendRequest(requestId));
    }

    // Reject friend request
    @PatchMapping("/requests/{requestId}/reject")
    public ResponseEntity<FriendRequestResponse> rejectFriendRequest(
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(friendService.rejectFriendRequest(requestId));
    }

    // Incoming requests
    @GetMapping("/requests/incoming")
    public ResponseEntity<List<FriendRequestResponse>> getIncomingRequests() {
        return ResponseEntity.ok(friendService.getIncomingRequests());
    }

    // Outgoing requests
    @GetMapping("/requests/outgoing")
    public ResponseEntity<List<FriendRequestResponse>> getOutgoingRequests() {
        return ResponseEntity.ok(friendService.getOutgoingRequests());
    }

    // Friends list
    @GetMapping
    public ResponseEntity<List<FriendResponse>> getFriends() {
        return ResponseEntity.ok(friendService.getFriends());
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable Integer friendId
    ) {
        friendService.removeFriend(friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchUserResponse>> searchUsers(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(
                friendService.searchUsers(keyword)
        );
    }

    @DeleteMapping("/friend-requests/{requestId}")
    public ResponseEntity<Void> cancelFriendRequest(
            @PathVariable Long requestId
    ) {

        friendService.cancelFriendRequest(
                requestId
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}
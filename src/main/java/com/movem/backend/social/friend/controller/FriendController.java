package com.movem.backend.social.friend.controller;

import com.movem.backend.social.friend.dtos.request.SendFriendRequestRequest;
import com.movem.backend.social.friend.dtos.response.FriendRequestResponse;
import com.movem.backend.social.friend.dtos.response.FriendResponse;
import com.movem.backend.social.friend.dtos.response.SearchUserResponse;
import com.movem.backend.social.friend.services.FriendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@Tag(name = "Social - Friends", description = "Add, Confirm/Reject Friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<FriendRequestResponse> sendFriendRequest(@Valid @RequestBody SendFriendRequestRequest request) {
        return ResponseEntity.ok(friendService.sendFriendRequest(request));
    }

    @PatchMapping("/requests/{requestId}/accept")
    public ResponseEntity<FriendRequestResponse> acceptFriendRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(friendService.acceptFriendRequest(requestId));
    }

    @PatchMapping("/requests/{requestId}/reject")
    public ResponseEntity<FriendRequestResponse> rejectFriendRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(friendService.rejectFriendRequest(requestId));
    }

    @GetMapping("/requests/incoming")
    public ResponseEntity<List<FriendRequestResponse>> getIncomingRequests() {
        return ResponseEntity.ok(friendService.getIncomingRequests());
    }

    @GetMapping("/requests/outgoing")
    public ResponseEntity<List<FriendRequestResponse>> getOutgoingRequests() {
        return ResponseEntity.ok(friendService.getOutgoingRequests());
    }

    @GetMapping
    public ResponseEntity<List<FriendResponse>> getFriends() {
        return ResponseEntity.ok(friendService.getFriends());
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Integer friendId) {
        friendService.removeFriend(friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchUserResponse>> searchUsers(@RequestParam String keyword) {
        return ResponseEntity.ok(friendService.searchUsers(keyword));
    }

    @DeleteMapping("/friend-requests/{requestId}")
    public ResponseEntity<Void> cancelFriendRequest(@PathVariable Long requestId) {
        friendService.cancelFriendRequest(requestId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<SearchUserResponse>> getSuggestedFriends() {
        return ResponseEntity.ok(friendService.getSuggestedFriends());
    }
}
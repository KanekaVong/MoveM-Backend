package com.movem.backend.Service.Implement.FriendServices;


import com.movem.backend.Dto.request.FriendRequest.SendFriendRequestRequest;
import com.movem.backend.Dto.response.FriendResponse.FriendRequestResponse;
import com.movem.backend.Dto.response.FriendResponse.FriendResponse;
import com.movem.backend.Dto.response.FriendResponse.SearchUserResponse;
import com.movem.backend.Entity.Friend.Friend;
import com.movem.backend.Entity.Friend.FriendRequest;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.DuplicateResourceException;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Exception.UnauthorizedActionException;
import com.movem.backend.Mapper.FriendMapper.FriendMapper;
import com.movem.backend.Mapper.FriendMapper.FriendRequestMapper;
import com.movem.backend.Service.NotificationServices.NotificationService;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Friend.FriendRequestStatus;
import com.movem.backend.model.enums.Friend.FriendStatus;
import com.movem.backend.Repository.AuthRepository.UserRepository;
import com.movem.backend.Repository.FriendRepository.FriendRepository;
import com.movem.backend.Repository.FriendRepository.FriendRequestRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.SharedServices.AuditLogService;
import com.movem.backend.Service.FriendServices.FriendService;
import com.movem.backend.model.enums.Notification.NotificationType;
import com.movem.backend.model.enums.Notification.ReferenceType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final FriendMapper friendMapper;
    private final FriendRequestMapper friendRequestMapper;

    @Override
    public FriendRequestResponse sendFriendRequest(
            SendFriendRequestRequest request
    ) {

        User sender = currentUserService.getCurrentUser();

        User receiver = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        if(sender.getId().equals(receiver.getId())){
            throw new IllegalArgumentException(
                    "You cannot send a friend request to yourself."
            );
        }

        if(areFriends(sender, receiver)){
            throw new DuplicateResourceException(
                    "You are already friends."
            );
        }

        if (friendRequestRepository.findBySenderAndReceiverAndStatus(
                sender,
                receiver,
                FriendRequestStatus.PENDING
        ).isPresent()) {

            throw new DuplicateResourceException(
                    "Friend request already sent."
            );
        }

        // Receiver -> Sender
        if (friendRequestRepository.findBySenderAndReceiverAndStatus(
                receiver,
                sender,
                FriendRequestStatus.PENDING
        ).isPresent()) {

            throw new DuplicateResourceException(
                    "This user has already sent you a friend request."
            );
        }


        FriendRequest friendRequest = new FriendRequest();

        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(FriendRequestStatus.PENDING);

        FriendRequest saved =
                friendRequestRepository.save(friendRequest);

        notificationService.createNotification(
                receiver,
                sender,
                NotificationType.FRIEND_REQUEST,
                ReferenceType.USER,
                sender.getId().toString(),
                "New Friend Request",
                sender.getUsername() +
                        " sent you a friend request."
        );

        auditLogService.createLog(
                null,
                sender,
                ActivityFeedEvent.FRIEND_REQUEST_SENT,
                AuditCategory.FRIEND,
                AuditSeverity.INFO,
                "friend_request",
                "Sent friend request.",
                null,
                receiver.getUsername()
        );

        return friendMapper.toFriendRequestResponse(saved);

    }

    @Override
    public FriendRequestResponse acceptFriendRequest(Long requestId) {

        User currentUser = currentUserService.getCurrentUser();

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Friend request not found."
                        ));

        // Only receiver can accept
        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException(
                    "You are not allowed to accept this request."
            );
        }

        // Already handled?
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new IllegalArgumentException(
                    "This friend request has already been processed."
            );
        }

        // Create friendship
        createFriend(
                request.getSender(),
                request.getReceiver()
        );

        // Update request
        request.setStatus(FriendRequestStatus.ACCEPTED);
        request.setRespondedAt(LocalDateTime.now());

        FriendRequest saved =
                friendRequestRepository.save(request);

        notificationService.createNotification(
                request.getSender(),
                currentUser,
                NotificationType.FRIEND_ACCEPTED,
                ReferenceType.USER,
                currentUser.getId().toString(),
                "Friend Request Accepted",
                currentUser.getUsername() +
                        " accepted your friend request."
        );

        auditLogService.createLog(
                null,
                currentUser,
                ActivityFeedEvent.FRIEND_REQUEST_ACCEPTED,
                AuditCategory.FRIEND,
                AuditSeverity.INFO,
                "status",
                "Accepted friend request.",
                null,
                request.getSender().getUsername()
        );

        return friendMapper.toFriendRequestResponse(saved);
    }

    @Override
    public FriendRequestResponse rejectFriendRequest(Long requestId) {

        User currentUser = currentUserService.getCurrentUser();

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Friend request not found."
                        ));

        // Only receiver can reject
        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException(
                    "You are not allowed to reject this request."
            );
        }

        // Already handled?
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new IllegalArgumentException(
                    "This friend request has already been processed."
            );
        }

        request.setStatus(FriendRequestStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());

        FriendRequest saved = friendRequestRepository.save(request);

        auditLogService.createLog(
                null,
                currentUser,
                ActivityFeedEvent.FRIEND_REQUEST_REJECTED,
                AuditCategory.FRIEND,
                AuditSeverity.INFO,
                "status",
                "Rejected friend request.",
                null,
                request.getSender().getUsername()
        );

        return friendMapper.toFriendRequestResponse(saved);
    }

    @Override
    @Transactional
    public FriendRequestResponse cancelFriendRequest(
            Long requestId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FriendRequest friendRequest =
                friendRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Friend request not found."
                                )
                        );

        /*
         * Only the sender can cancel the request.
         */
        if (
                !friendRequest.getSender()
                        .getId()
                        .equals(currentUser.getId())
        ) {

            throw new IllegalArgumentException(
                    "You can only cancel your own friend request."
            );
        }
        if (
                friendRequest.getStatus()
                        != FriendRequestStatus.PENDING
        ) {

            throw new IllegalArgumentException(
                    "Only pending friend requests can be cancelled."
            );
        }

        /*
         * Change request status.
         */
        friendRequest.setStatus(
                FriendRequestStatus.CANCELLED
        );

        friendRequest.setRespondedAt(
                LocalDateTime.now()
        );

        FriendRequest saved =
                friendRequestRepository.save(
                        friendRequest
                );

        return friendRequestMapper.toResponse(
                saved
        );
    }

    @Override
    public List<FriendRequestResponse> getIncomingRequests() {

        User currentUser = currentUserService.getCurrentUser();

        return friendRequestRepository
                .findByReceiverIdAndStatusOrderByCreatedAtDesc(
                        currentUser.getId(),
                        FriendRequestStatus.PENDING
                )
                .stream()
                .map(friendMapper::toFriendRequestResponse)
                .toList();
    }

    @Override
    public List<FriendRequestResponse> getOutgoingRequests() {

        User currentUser = currentUserService.getCurrentUser();

        return friendRequestRepository
                .findBySenderIdAndStatusOrderByCreatedAtDesc(
                        currentUser.getId(),
                        FriendRequestStatus.PENDING
                )
                .stream()
                .map(friendMapper::toFriendRequestResponse)
                .toList();
    }



    @Override
    public void removeFriend(Integer friendId) {

        User currentUser = currentUserService.getCurrentUser();

        User friend = userRepository.findById(friendId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        User first = first(currentUser, friend);
        User second = second(currentUser, friend);

        Friend relationship = friendRepository
                .findByUserOneIdAndUserTwoId(
                        first.getId(),
                        second.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Friendship not found."
                        ));

        friendRepository.delete(relationship);

        friendRequestRepository.deleteRequestsBetweenUsers(
                currentUser,
                friend
        );

        auditLogService.createLog(
                null,
                currentUser,
                ActivityFeedEvent.FRIEND_REMOVED,
                AuditCategory.FRIEND,
                AuditSeverity.WARNING,
                "friend",
                "Removed friend.",
                friend.getUsername(),
                null
        );

    }

    @Override
    public List<FriendResponse> getFriends() {

        User currentUser = currentUserService.getCurrentUser();

        return friendRepository
                .findByUserOneIdOrUserTwoId(
                        currentUser.getId(),
                        currentUser.getId()
                )
                .stream()
                .map(friend ->
                        friendMapper.toFriendResponse(
                                friend,
                                currentUser.getId()
                        )
                )
                .toList();
    }

    @Override
    public List<SearchUserResponse> searchUsers(String keyword) {

        User currentUser = currentUserService.getCurrentUser();

        return userRepository
                .findByUsernameContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword
                )
                .stream()

                // Don't show yourself
                .filter(user ->
                        !user.getId().equals(currentUser.getId())
                )

                .map(user -> friendMapper.toSearchUserResponse(
                        user,
                        determineFriendStatus(currentUser, user)
                ))

                .toList();
    }

    private Friend createFriend(User a, User b){

        User first = first(a, b);
        User second = second(a, b);

        Friend friend = new Friend();

        friend.setUserOne(first);
        friend.setUserTwo(second);

        return friendRepository.save(friend);
    }

    private boolean areFriends(User a, User b){

        User first = a.getId() < b.getId() ? a : b;
        User second = a.getId() < b.getId() ? b : a;

        return friendRepository.existsByUserOneAndUserTwo(first, second);
    }

    private FriendStatus determineFriendStatus(
            User currentUser,
            User otherUser
    ) {

        // Check if already friends
        User first = currentUser.getId() < otherUser.getId()
                ? currentUser
                : otherUser;

        User second = currentUser.getId() < otherUser.getId()
                ? otherUser
                : currentUser;

        if (friendRepository.findByUserOneAndUserTwo(first, second).isPresent()) {
            return FriendStatus.FRIEND;
        }

        // Did I send them a request?
        if (friendRequestRepository
                .findBySenderAndReceiverAndStatus(
                        currentUser,
                        otherUser,
                        FriendRequestStatus.PENDING)
                .isPresent()) {

            return FriendStatus.PENDING_REQUEST;
        }

        // Did they send me a request?
        if (friendRequestRepository
                .findBySenderAndReceiverAndStatus(
                        otherUser,
                        currentUser,
                        FriendRequestStatus.PENDING)
                .isPresent()) {

            return FriendStatus.REQUEST_RECEIVED;
        }

        return FriendStatus.NONE;
    }

    private User first(User a, User b){
        return a.getId() < b.getId() ? a : b;
    }

    private User second(User a, User b){
        return a.getId() < b.getId() ? b : a;
    }


}

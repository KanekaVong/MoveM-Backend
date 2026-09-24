package com.movem.backend.social.friend.services;

import com.movem.backend.social.friend.dtos.request.SendFriendRequestRequest;
import com.movem.backend.social.friend.dtos.response.FriendRequestResponse;
import com.movem.backend.social.friend.dtos.response.FriendResponse;
import com.movem.backend.social.friend.dtos.response.SearchUserResponse;

import java.util.List;

public interface FriendService {

    FriendRequestResponse sendFriendRequest(
            SendFriendRequestRequest request
    );

    FriendRequestResponse acceptFriendRequest(
            Long requestId
    );

    FriendRequestResponse cancelFriendRequest(
            Long requestId
    );

    FriendRequestResponse rejectFriendRequest(
            Long requestId
    );

    List<FriendRequestResponse> getIncomingRequests();

    List<FriendRequestResponse> getOutgoingRequests();

    List<FriendResponse> getFriends();


    void removeFriend(Integer userId);

    List<SearchUserResponse> searchUsers(String keyword);

    List<SearchUserResponse> getSuggestedFriends();
}

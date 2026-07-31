package com.movem.backend.service.SocialServices;

import com.movem.backend.dto.request.FriendRequest.SendFriendRequestRequest;
import com.movem.backend.dto.response.FriendResponse.FriendRequestResponse;
import com.movem.backend.dto.response.FriendResponse.FriendResponse;
import com.movem.backend.dto.response.FriendResponse.SearchUserResponse;

import java.util.List;

public interface FriendService {

    FriendRequestResponse sendFriendRequest(
            SendFriendRequestRequest request
    );

    FriendRequestResponse acceptFriendRequest(
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
}

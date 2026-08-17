package com.movem.backend.Service.FriendServices;

import com.movem.backend.Dto.request.FriendRequest.SendFriendRequestRequest;
import com.movem.backend.Dto.response.FriendResponse.FriendRequestResponse;
import com.movem.backend.Dto.response.FriendResponse.FriendResponse;
import com.movem.backend.Dto.response.FriendResponse.SearchUserResponse;

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

}

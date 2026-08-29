package com.movem.backend.Mapper.FriendMapper;


import com.movem.backend.Dto.response.FriendResponse.FriendRequestResponse;
import com.movem.backend.Dto.response.FriendResponse.FriendResponse;
import com.movem.backend.Dto.response.FriendResponse.SearchUserResponse;
import com.movem.backend.Entity.Friend.Friend;
import com.movem.backend.Entity.Friend.FriendRequest;
import com.movem.backend.Entity.Auth.User;
import java.util.Base64;

import com.movem.backend.model.enums.Friend.FriendStatus;
import org.springframework.stereotype.Component;

@Component
public class FriendMapper {

    public FriendRequestResponse toFriendRequestResponse(FriendRequest request) {

        FriendRequestResponse response = new FriendRequestResponse();

        response.setRequestId(request.getId());

        response.setSenderId(request.getSender().getId());
        response.setSenderUsername(request.getSender().getUsername());

        if (request.getSender().getProfilePic() != null) {
            response.setSenderProfilePic(
                    request.getSender().getProfilePic()
            );
        }

        response.setReceiverId(request.getReceiver().getId());
        response.setReceiverUsername(request.getReceiver().getUsername());

        response.setStatus(request.getStatus());
        response.setCreatedAt(request.getCreatedAt());

        return response;
    }

    public FriendResponse toFriendResponse(User user) {

        FriendResponse response = new FriendResponse();

        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());

        if (user.getProfilePic() != null) {
            response.setProfilePic(user.getProfilePic());
        }

        return response;
    }

    public FriendResponse toFriendResponse(Friend friend, Integer currentUserId) {

        User friendUser;

        if(friend.getUserOne().getId().equals(currentUserId)){
            friendUser = friend.getUserTwo();
        }else{
            friendUser = friend.getUserOne();
        }

        return toFriendResponse(friendUser);
    }

    public SearchUserResponse toSearchUserResponse(
            User user,
            FriendStatus friendStatus
    ) {

        SearchUserResponse response = new SearchUserResponse();

        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());

        if (user.getProfilePic() != null) {
            response.setProfilePic(user.getProfilePic());
        }

        response.setFriendStatus(friendStatus);

        return response;
    }

}
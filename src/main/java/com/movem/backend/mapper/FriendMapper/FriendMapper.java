package com.movem.backend.mapper.FriendMapper;


import com.movem.backend.dto.response.FriendResponse.FriendRequestResponse;
import com.movem.backend.dto.response.FriendResponse.FriendResponse;
import com.movem.backend.dto.response.FriendResponse.SearchUserResponse;
import com.movem.backend.entity.Friend.Friend;
import com.movem.backend.entity.Friend.FriendRequest;
import com.movem.backend.entity.User;
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
                    Base64.getEncoder().encodeToString(
                            request.getSender().getProfilePic()
                    )
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
            response.setProfilePic(
                    Base64.getEncoder().encodeToString(
                            user.getProfilePic()
                    )
            );
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
            response.setProfilePic(
                    Base64.getEncoder()
                            .encodeToString(user.getProfilePic())
            );
        }

        response.setFriendStatus(friendStatus);

        return response;
    }

}
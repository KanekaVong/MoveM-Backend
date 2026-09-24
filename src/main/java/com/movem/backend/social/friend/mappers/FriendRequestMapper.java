package com.movem.backend.social.friend.mappers;

import com.movem.backend.social.friend.dtos.response.FriendRequestResponse;
import com.movem.backend.social.friend.entities.FriendRequest;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestMapper {

    public FriendRequestResponse toResponse(
            FriendRequest friendRequest
    ) {

        FriendRequestResponse response =
                new FriendRequestResponse();

        response.setRequestId(
                friendRequest.getId()
        );

        response.setSenderId(
                friendRequest.getSender() != null
                        ? friendRequest.getSender().getId()
                        : null
        );

        response.setSenderUsername(
                friendRequest.getSender() != null
                        ? friendRequest.getSender().getUsername()
                        : null
        );

        response.setSenderProfilePic(
                friendRequest.getSender() != null
                        ? friendRequest.getSender().getProfilePic()
                        : null
        );

        response.setReceiverId(
                friendRequest.getReceiver() != null
                        ? friendRequest.getReceiver().getId()
                        : null
        );

        response.setReceiverUsername(
                friendRequest.getReceiver() != null
                        ? friendRequest.getReceiver().getUsername()
                        : null
        );

        response.setStatus(
                friendRequest.getStatus()
        );

        response.setCreatedAt(
                friendRequest.getCreatedAt()
        );

        return response;
    }
}
package com.movem.backend.Mapper.FriendMapper;

import com.movem.backend.Dto.response.FriendResponse.FriendRequestResponse;
import com.movem.backend.Entity.Friend.FriendRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
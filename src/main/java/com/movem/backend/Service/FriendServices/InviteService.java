package com.movem.backend.Service.FriendServices;


import com.movem.backend.Dto.response.FriendResponse.InviteResponse;

public interface InviteService {

    InviteResponse createInvite();

    InviteResponse getInvite(String token);

    void acceptInvite(
            String token
    );
}
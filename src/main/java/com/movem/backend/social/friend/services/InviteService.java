package com.movem.backend.social.friend.services;

import com.movem.backend.social.friend.dtos.response.InviteResponse;

public interface InviteService {

    InviteResponse createInvite();
    InviteResponse getInvite(String token);

    void acceptInvite(String token);
}
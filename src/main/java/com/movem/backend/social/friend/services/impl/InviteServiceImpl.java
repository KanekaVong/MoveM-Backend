package com.movem.backend.social.friend.services.impl;

import com.movem.backend.social.friend.dtos.response.InviteResponse;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.social.friend.entities.Invite;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.authentication.repositories.UserRepository;
import com.movem.backend.social.friend.repositories.InviteRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.social.friend.services.InviteService;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InviteServiceImpl
        implements InviteService {

    private final InviteRepository inviteRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    private static final String INVITE_BASE_URL =
            "https://movem.app/invite/";

    @Override
    public InviteResponse createInvite() {

        User currentUser =
                currentUserService.getCurrentUser();

        Invite existingInvite =
                inviteRepository
                        .findByInvitedBy(
                                currentUser
                        )
                        .orElse(null);

        if (existingInvite != null) {

            return toResponse(existingInvite);
        }

        String token =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "");

        LocalDateTime now =
                LocalDateTime.now();

        Invite invite =
                Invite.builder()
                        .token(token)
                        .invitedBy(currentUser)
                        .createdAt(now)
                        .expiresAt(
                                now.plusDays(7)
                        )
                        .build();

        Invite saved =
                inviteRepository.save(invite);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InviteResponse getInvite(
            String token
    ) {

        Invite invite =
                inviteRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Invite not found."
                                )
                        );

        validateInvite(invite);

        return toResponse(invite);
    }

    @Override
    public void acceptInvite(String token) {

        User currentUser =
                currentUserService.getCurrentUser();

        Invite invite =
                inviteRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Invite not found."
                                )
                        );

        if (
                invite.getExpiresAt() != null &&
                        invite.getExpiresAt()
                                .isBefore(LocalDateTime.now())
        ) {
            throw new IllegalArgumentException(
                    "This invite has expired."
            );
        }

        if (
                invite.getInvitedBy()
                        .getId()
                        .equals(currentUser.getId())
        ) {
            throw new IllegalArgumentException(
                    "You cannot accept your own invite."
            );
        }

        // We'll create the actual friendship here later.
    }

    private void validateInvite(
            @MonotonicNonNull Invite invite
    ) {

        if (
                invite.getExpiresAt() != null
                        && invite.getExpiresAt()
                        .isBefore(LocalDateTime.now())
        ) {

            throw new IllegalArgumentException(
                    "This invite has expired."
            );
        }
    }

    private InviteResponse toResponse(
            @MonotonicNonNull Invite invite
    ) {

        return InviteResponse.builder()
                .id(invite.getId())
                .inviteUrl(
                        INVITE_BASE_URL
                                + invite.getToken()
                )
                .createdAt(
                        invite.getCreatedAt()
                )
                .expiresAt(
                        invite.getExpiresAt()
                )
                .build();
    }
}
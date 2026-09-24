package com.movem.backend.social.friend.repositories;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.social.friend.entities.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteRepository
        extends JpaRepository<Invite, Long> {

    Optional<Invite> findByToken(String token);

    Optional<Invite> findByInvitedBy(User user);
}
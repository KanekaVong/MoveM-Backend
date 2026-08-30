package com.movem.backend.Repository.FriendRepository;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Friend.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteRepository
        extends JpaRepository<Invite, Long> {

    Optional<Invite> findByToken(String token);

    Optional<Invite> findByInvitedBy(User user);
}
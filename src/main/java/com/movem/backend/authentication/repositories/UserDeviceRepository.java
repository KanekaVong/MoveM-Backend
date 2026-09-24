package com.movem.backend.authentication.repositories;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.authentication.entities.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository
        extends JpaRepository<UserDevice, Long> {

    List<UserDevice> findByUserAndIsActiveTrue(
            User user
    );

    Optional<UserDevice> findByDeviceToken(
            String deviceToken
    );

    Optional<UserDevice> findByUserAndDeviceToken(
            User user,
            String deviceToken
    );
}
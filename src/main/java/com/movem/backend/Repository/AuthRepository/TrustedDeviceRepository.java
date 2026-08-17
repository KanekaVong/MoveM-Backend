package com.movem.backend.Repository.AuthRepository;

import com.movem.backend.Entity.Auth.TrustedDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrustedDeviceRepository extends JpaRepository<TrustedDevice, Integer> {

    Optional<TrustedDevice> findByJti(String jti);

    Optional<TrustedDevice> findByUserIdAndDeviceIdAndRevokedAtIsNull(
            Integer userId,
            String deviceId
    );

    List<TrustedDevice> findByUserIdAndRevokedAtIsNull(Integer userId);
}
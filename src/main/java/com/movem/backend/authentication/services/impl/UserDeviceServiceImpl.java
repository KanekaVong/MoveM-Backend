package com.movem.backend.authentication.services.impl;

import com.movem.backend.authentication.dtos.requests.RegisterDeviceRequest;
import com.movem.backend.authentication.dtos.responses.UserDeviceResponse;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.authentication.entities.UserDevice;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.authentication.repositories.UserDeviceRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.authentication.services.UserDeviceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDeviceServiceImpl
        implements UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;
    private final CurrentUserService currentUserService;


    @Override
    public UserDeviceResponse registerDevice(RegisterDeviceRequest request) {

        User currentUser = currentUserService.getCurrentUser();
        UserDevice device = userDeviceRepository.findByDeviceToken(request.getDeviceToken()).orElse(null);

        LocalDateTime now = LocalDateTime.now();


        if (device == null) {
            device = new UserDevice();
            device.setUser(currentUser);
            device.setDeviceToken(request.getDeviceToken());
            device.setPlatform(request.getPlatform());
            device.setIsActive(true);
            device.setCreatedAt(now);

        } else {
            device.setUser(currentUser);
            device.setPlatform(request.getPlatform());
            device.setIsActive(true);
        }

        device.setLastSeenAt(now);
        device.setUpdatedAt(now);


        UserDevice saved = userDeviceRepository.save(device);

        return toResponse(saved);
    }


    @Override
    @Transactional
    public List<UserDeviceResponse> getMyDevices() {

        User currentUser = currentUserService.getCurrentUser();

        return userDeviceRepository
                .findByUserAndIsActiveTrue(currentUser)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Override
    public void deactivateDevice(
            Long deviceId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        UserDevice device =
                userDeviceRepository
                        .findById(deviceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Device not found."
                                )
                        );


        if (
                !device.getUser()
                        .getId()
                        .equals(currentUser.getId())
        ) {

            throw new IllegalArgumentException(
                    "You can only deactivate your own device."
            );
        }


        device.setIsActive(false);
        device.setUpdatedAt(
                LocalDateTime.now()
        );

        userDeviceRepository.save(device);
    }


    private UserDeviceResponse toResponse(
            UserDevice device
    ) {

        return UserDeviceResponse.builder()
                .id(device.getId())
                .userId(device.getUser().getId())
                .platform(device.getPlatform())
                .isActive(device.getIsActive())
                .lastSeenAt(device.getLastSeenAt())
                .build();
    }
}
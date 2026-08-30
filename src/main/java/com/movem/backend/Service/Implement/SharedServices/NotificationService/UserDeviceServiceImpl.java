package com.movem.backend.Service.Implement.SharedServices.NotificationService;

import com.movem.backend.Dto.request.NotificationRequest.RegisterDeviceRequest;
import com.movem.backend.Dto.response.NotificationResponses.UserDeviceResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Auth.UserDevice;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.NotificationRepository.UserDeviceRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.NotificationServices.UserDeviceService;
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
    public UserDeviceResponse registerDevice(
            RegisterDeviceRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        UserDevice device =
                userDeviceRepository
                        .findByDeviceToken(
                                request.getDeviceToken()
                        )
                        .orElse(null);

        LocalDateTime now =
                LocalDateTime.now();


        if (device == null) {

            device = new UserDevice();

            device.setUser(currentUser);

            device.setDeviceToken(
                    request.getDeviceToken()
            );

            device.setPlatform(
                    request.getPlatform()
            );

            device.setIsActive(true);

            device.setCreatedAt(now);

        } else {

            /*
             * Token already exists.
             * Re-associate it with the currently
             * authenticated user and reactivate it.
             */
            device.setUser(currentUser);

            device.setPlatform(
                    request.getPlatform()
            );

            device.setIsActive(true);
        }


        device.setLastSeenAt(now);
        device.setUpdatedAt(now);


        UserDevice saved =
                userDeviceRepository.save(device);

        return toResponse(saved);
    }


    @Override
    @Transactional
    public List<UserDeviceResponse> getMyDevices() {

        User currentUser =
                currentUserService.getCurrentUser();

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
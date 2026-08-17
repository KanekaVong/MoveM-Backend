package com.movem.backend.Service.NotificationServices;

import com.movem.backend.Dto.request.NotificationRequest.RegisterDeviceRequest;
import com.movem.backend.Dto.response.NotificationResponses.UserDeviceResponse;

import java.util.List;

public interface UserDeviceService {

    UserDeviceResponse registerDevice(
            RegisterDeviceRequest request
    );

    List<UserDeviceResponse> getMyDevices();

    void deactivateDevice(
            Long deviceId
    );
}
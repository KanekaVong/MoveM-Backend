package com.movem.backend.authentication.services;

import com.movem.backend.authentication.dtos.requests.RegisterDeviceRequest;
import com.movem.backend.authentication.dtos.responses.UserDeviceResponse;

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
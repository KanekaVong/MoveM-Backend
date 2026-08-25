package com.movem.backend.Controller.AuthController;

import com.movem.backend.Dto.response.AuthResponses.UserResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Hidden
public class TestController {

    @GetMapping("/api/test/secure")
    public String secureEndpoint(Authentication authentication) {
        return "Hello, " + authentication.getName() + "! You are authenticated.";
    }

    @GetMapping("/api/test/user-response")
    public ResponseEntity<UserResponse> testUserResponse(Authentication authentication) {
        // Mocking a UserResponse to show you how the JSON looks
        UserResponse mockUser = new UserResponse();
        mockUser.setId(1L);
        mockUser.setUsername(authentication.getName());
        mockUser.setFirstname("John");
        mockUser.setLastname("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setCityProvince("Phnom Penh");
        mockUser.setDateOfBirth(LocalDate.of(2000, 1, 1));
        mockUser.setJoinDate(LocalDate.now());
        mockUser.setThemePreference("DARK");
        mockUser.setLanguagePreference("EN");
        
        // Notice: No password is set here, so it won't be in the JSON!
        
        return ResponseEntity.ok(mockUser);
    }
}
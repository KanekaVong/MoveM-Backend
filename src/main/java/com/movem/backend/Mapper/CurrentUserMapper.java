package com.movem.backend.Mapper;

import com.movem.backend.Dto.response.AuthResponses.CurrentUserResponse;
import com.movem.backend.Entity.Auth.User;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserMapper {

    public CurrentUserResponse toResponse(User user) {

        return CurrentUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .dateOfBirth(user.getDateOfBirth())
                .jointDate(user.getJointDate())
                .profilePic(user.getProfilePic())
                .gender(user.getGender())
                .cityProvince(user.getCityProvince())
                .isActive(user.getIsActive())
                .themePreference(user.getThemePreference())
                .languagePreference(user.getLanguagePreference())
                .build();
    }
}
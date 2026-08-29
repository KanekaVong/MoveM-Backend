package com.movem.backend.Mapper;

import com.movem.backend.Dto.response.AuthResponses.UserResponse;
import com.movem.backend.Entity.Auth.User;
import org.springframework.stereotype.Component;


@Component
public class CurrentUserMapper {

    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .bio(user.getBio())
                .phone(user.getPhone())
                .cityProvince(user.getCityProvince())
                .dateOfBirth(user.getDateOfBirth())
                .jointDate(user.getJointDate())
                .themePreference(user.getThemePreference())
                .languagePreference(user.getLanguagePreference())
                .profilePic(user.getProfilePic())
                .gender(user.getGender())
                .isActive(user.getIsActive())
                .build();
    }
}
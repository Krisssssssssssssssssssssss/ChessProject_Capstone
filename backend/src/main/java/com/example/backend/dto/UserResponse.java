package com.example.backend.dto;

import com.example.backend.model.UserModel;
import lombok.Builder;

@Builder
public record UserResponse(String id, String name, String password, String rating, boolean isAdmin, boolean isGitHubUser){
    public static UserResponse from(UserModel user) {
    return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .isGitHubUser(user.getIsGitHubUser())
            .password(user.getPassword())
            .rating(user.getRating())
            .isAdmin(user.getIsAdmin() != null ? user.getIsAdmin() : false)
            .build();
    }
}

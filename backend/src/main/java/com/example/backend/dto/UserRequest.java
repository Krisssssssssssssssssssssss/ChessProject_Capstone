package com.example.backend.dto;

import com.example.backend.model.UserModel;
import lombok.NonNull;

public record UserRequest(
        @NonNull String name,
        @NonNull String password,
        @NonNull Boolean isGitHubUser,
        String rating,
        Boolean isAdmin
) {
    public UserModel toModel() {
        return UserModel.builder()
                .name(name)
                .password(password)
                .isGitHubUser(isGitHubUser)
                .rating(rating != null ? rating : "1500") // Default rating if null
                .isAdmin(isAdmin != null && isAdmin) // Default admin status if null
                .build();
    }
}

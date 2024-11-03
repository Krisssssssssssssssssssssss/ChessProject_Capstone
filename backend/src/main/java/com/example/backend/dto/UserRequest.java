package com.example.backend.dto;

import com.example.backend.model.UserModel;
import lombok.NonNull;

public record UserRequest(
        @NonNull String name,
        @NonNull String password
) {
    public UserModel toModel() {
        return UserModel.builder()
                .name(name)
                .password(password)
                .rating(String.valueOf(1500))
                .isAdmin(false)
                .build();
    }
}

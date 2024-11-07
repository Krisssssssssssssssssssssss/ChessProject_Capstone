package com.example.backend.controller;
import com.example.backend.dto.UserResponse;
import com.example.backend.model.UserModel;
import com.example.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/me")
    public String getMe(@AuthenticationPrincipal OAuth2User user) throws Exception {
        return user == null ? "" : user.getAttributes().get("login").toString();
    }
}

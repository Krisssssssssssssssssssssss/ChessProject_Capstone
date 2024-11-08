package com.example.backend.controller;

import com.example.backend.dto.UserRequest;
import com.example.backend.dto.UserResponse;
import com.example.backend.model.UserModel;
import com.example.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest userRequest) throws Exception {
        UserModel user = userRequest.toModel();
        return UserResponse.from(userService.save(user));
    }

    @GetMapping
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserModel getUser(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/find_by_name/{name}")
    public UserModel getUserbyName(@PathVariable String name) {
        return userService.getUserByName(name);
    }

    @PutMapping("/{id}")
    public UserResponse editUser(@PathVariable String id, @RequestBody UserRequest userRequest) throws Exception {
        UserModel user = userRequest.toModel();
        user.setId(id);
        return UserResponse.from(userService.editUser(user));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public UserResponse getUserByLogin(@RequestBody UserRequest userRequest) {
        UserModel user = userRequest.toModel();
        return UserResponse.from(userService.getUserByName_and_Password(user));
    }
}

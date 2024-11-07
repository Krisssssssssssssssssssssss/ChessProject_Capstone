package com.example.backend.service;

import com.example.backend.dto.UserRequest;
import com.example.backend.dto.UserResponse;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.model.UserModel;
import com.example.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final IdService idService;

    public UserModel save(UserModel userModel) throws Exception {
        Optional<UserModel> existingUser = userRepository.findByName(userModel.getName());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        if (userModel.getName().equals("admin")) {
            userModel.setId(String.valueOf(1));
            userModel.setIsAdmin(true);
        } else {
            userModel.setId(idService.getRandomId());
        }
        return userRepository.save(userModel);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel getUserById(String id) {
        return userRepository.findById(id).orElseThrow();
    }

    public UserModel getUserByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    public UserModel getUserByName_and_Password(UserModel user) {
        return userRepository.findByNameAndPassword(user.getName(), user.getPassword()).orElseThrow();
    }

    public UserModel editUser(UserModel user) throws Exception {
        userRepository.findById(user.getId()).orElseThrow();
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }
}

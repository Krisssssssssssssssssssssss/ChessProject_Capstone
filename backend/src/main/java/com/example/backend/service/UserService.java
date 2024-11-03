package com.example.backend.service;

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
        Optional<UserModel> existingUser = Optional.ofNullable(userRepository.findByName(userModel.getName()));

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        } else {
            userModel.setId(idService.getRandomId());
            return userRepository.save(userModel);
        }
    }


    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel getUserById(String id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user == null) {
            return null;
        }
        return user.get();
    }
}

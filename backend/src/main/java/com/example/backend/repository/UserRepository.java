package com.example.backend.repository;

import com.example.backend.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {
    Optional<UserModel> findByName(String name);
    Optional<UserModel> findByNameAndPassword(String name, String password);
}

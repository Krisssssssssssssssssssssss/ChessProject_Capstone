package com.example.backend.repository;

import com.example.backend.model.GameModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends MongoRepository<GameModel, String> {
    Optional<GameModel> findGameByPlayerOneIdAndPlayerTwoId(String playerOneId, String playerTwoId);
}
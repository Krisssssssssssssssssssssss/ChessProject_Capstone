package com.example.backend.service;

import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.model.GameModel;
import com.example.backend.repository.GameRepository;
import com.example.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final IdService idService;
    public GameModel createGame(GameModel gameModel) throws Exception {
        Optional<GameModel> existingGame = gameRepository.findGameByPlayerOneIdAndPlayerTwoId(gameModel.getPlayerOneId(), gameModel.getPlayerTwoId());

        if (existingGame.isPresent()) {
            throw new UserAlreadyExistsException("Game already exists");
        }
        gameModel.setId(idService.getRandomId());
        return gameRepository.save(gameModel);
    }
    public GameModel getGame(String playerId, String playerTwoId)  {
        return gameRepository.findGameByPlayerOneIdAndPlayerTwoId(playerId, playerTwoId).orElseThrow();
    }
    public boolean doesGameExist(String playerId, String playerTwoId) {
        Optional<GameModel> existingGame = gameRepository.findGameByPlayerOneIdAndPlayerTwoId(playerId, playerTwoId);
        return existingGame.isPresent();
    }
}

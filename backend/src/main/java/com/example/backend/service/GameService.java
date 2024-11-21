package com.example.backend.service;

import com.example.backend.dto.piece_movement.MakeMoveRequest;
import com.example.backend.exception.GameNotFoundException;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.model.CastlingModel;
import com.example.backend.model.GameModel;
import com.example.backend.model.Tile;
import com.example.backend.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final IdService idService;
    public static boolean kingIsCastling = false;
    public static CastlingModel localCastling;

    public GameModel createGame(GameModel gameModel) throws Exception {
        Optional<GameModel> existingGame = gameRepository.findGameByPlayerOneIdAndPlayerTwoId(gameModel.getPlayerOneId(), gameModel.getPlayerTwoId());

        if (existingGame.isPresent()) {
            throw new UserAlreadyExistsException("Game already exists");
        }
        gameModel.setId(idService.getRandomId());
        return gameRepository.save(gameModel);
    }

    public GameModel getGame(String playerId, String playerTwoId) {
        return gameRepository.findGameByPlayerOneIdAndPlayerTwoId(playerId, playerTwoId)
                .orElseThrow(() -> new GameNotFoundException("Game not found for the selected players players"));
    }

    public boolean doesGameExist(String playerId, String playerTwoId) {
        Optional<GameModel> existingGame = gameRepository.findGameByPlayerOneIdAndPlayerTwoId(playerId, playerTwoId);
        return existingGame.isPresent();
    }

    public String makeMove(MakeMoveRequest makeMoveRequest) {
        GameModel game = fetchGame(makeMoveRequest.playerOneId(), makeMoveRequest.playerTwoId());
        localCastling = game.getCastlingModel();
        List<List<Tile>> gameBoard = FenConverter.toBoardArray(game.getFenString());

        if (!GameServiceHelper.isMoveAllowed(gameBoard, makeMoveRequest.sourceSquare(), makeMoveRequest.targetSquare(), game)) {
            return game.getFenString();
        }

        GameModel updatedGame = GameServiceHelper.updateGameState(game, gameBoard, makeMoveRequest);
        gameRepository.save(updatedGame);
        GameServiceHelper.resetTemporaryStates();
        return updatedGame.getFenString();
    }
    private GameModel fetchGame(String playerOneId, String playerTwoId) {
        return this.getGame(playerOneId, playerTwoId);
    }
   }
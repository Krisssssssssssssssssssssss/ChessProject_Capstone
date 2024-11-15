package com.example.backend.service;

import com.example.backend.exception.GameNotFoundException;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.model.GameModel;
import com.example.backend.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private static final String ID_FIRST = "John";
    private static final String ID_SECOND = "Jane";
    private static final String ID_THIRD = "Jack";

    private final GameRepository mockGameRepo = mock(GameRepository.class);
    private final IdService idService = new IdService();

    @Test
    @DirtiesContext
    void createGame() throws Exception {
        GameModel expectedGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_SECOND).build();

        when(mockGameRepo.findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND)).thenReturn(Optional.empty());
        when(mockGameRepo.save(expectedGame)).thenReturn(expectedGame);

        GameService gameService = new GameService(mockGameRepo, idService);
        GameModel result = gameService.createGame(expectedGame);

        verify(mockGameRepo).findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND);
        verify(mockGameRepo).save(expectedGame);

        assertEquals(expectedGame, result);
    }

    @Test
    @DirtiesContext
    void createGame_Fail() throws Exception {
        GameModel existingGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_SECOND).build();

        when(mockGameRepo.findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND))
                .thenReturn(Optional.of(existingGame));

        GameService gameService = new GameService(mockGameRepo, idService);
        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            gameService.createGame(existingGame);
        });
        verify(mockGameRepo).findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND);
        verify(mockGameRepo, never()).save(any(GameModel.class));

        assertEquals("Game already exists", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void getGame() throws Exception {
        GameModel expectedGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_SECOND).build();

        when(mockGameRepo.findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND)).thenReturn(Optional.of(expectedGame));

        GameService gameService = new GameService(mockGameRepo, idService);
        GameModel result = gameService.getGame(ID_FIRST, ID_SECOND);

        verify(mockGameRepo).findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND);

        assertEquals(expectedGame, result);
    }

    @Test
    @DirtiesContext
    void getGame_failed() {
        when(mockGameRepo.findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND))
                .thenReturn(Optional.empty());

        GameService gameService = new GameService(mockGameRepo, idService);

        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            gameService.getGame(ID_FIRST, ID_SECOND);
        });

        verify(mockGameRepo).findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND);
        assertEquals("Game not found for the selected players players", exception.getMessage());
    }


    @Test
    @DirtiesContext
    void doesGameExist() throws Exception {
        GameModel expectedGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_SECOND).build();

        when(mockGameRepo.findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND)).thenReturn(Optional.of(expectedGame));

        GameService gameService = new GameService(mockGameRepo, idService);
        GameModel result = gameService.getGame(ID_FIRST, ID_SECOND);

        verify(mockGameRepo).findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND);

        assertEquals(expectedGame, result);
    }

}
package com.example.backend.service;

import com.example.backend.dto.pieceMovement.MakeMoveRequest;
import com.example.backend.exception.GameNotFoundException;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.model.GameModel;
import com.example.backend.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

import org.mockito.MockedStatic;

class GameServiceTest {

    private static final String ID_FIRST = "John";
    private static final String ID_SECOND = "Jane";

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

    @Test
    @DirtiesContext
    void makeMove_isMoveAllowed_True() throws Exception {
        GameModel initialGame = GameModel.builder()
                .playerOneId(ID_FIRST)
                .playerTwoId(ID_SECOND)
                .fenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR")
                .isWhite(true) // White to move
                .build();

        MakeMoveRequest mockMakeMoveRequest = new MakeMoveRequest(
                ID_FIRST,
                ID_SECOND,
                "a7",
                "a6"
        );

        String updatedFen = "rnbqkbnr/1ppppppp/p7/8/8/8/PPPPPPPP/RNBQKBNR";

        GameModel updatedGame = GameModel.builder()
                .playerOneId(ID_FIRST)
                .playerTwoId(ID_SECOND)
                .fenString(updatedFen)
                .isWhite(false) // After move, Black to move
                .build();

        when(mockGameRepo.findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND))
                .thenReturn(Optional.of(initialGame));
        when(mockGameRepo.save(any(GameModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Mock the static methods in GameServiceHelpers
        try (MockedStatic<GameServiceHelpers> mockedHelpers = mockStatic(GameServiceHelpers.class)) {
            mockedHelpers.when(() -> GameServiceHelpers.isMoveAllowed(any(), any(), any(), any()))
                    .thenReturn(true);
            mockedHelpers.when(() -> GameServiceHelpers.updateGameState(any(), any(), any()))
                    .thenReturn(updatedGame);

            GameService gameService = new GameService(mockGameRepo, idService);

            String resultFen = gameService.makeMove(mockMakeMoveRequest);

            verify(mockGameRepo).findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND);
            verify(mockGameRepo).save(any(GameModel.class));
            assertEquals(updatedFen, resultFen);
        }
    }


    @Test
    @DirtiesContext
    void makeMove_isMoveAllowed_False() throws Exception {
        GameModel expectedGame = GameModel.builder()
                .playerOneId(ID_FIRST)
                .playerTwoId(ID_SECOND)
                .fenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR")
                .isWhite(true)
                .build();

        MakeMoveRequest mockMakeMoveRequest = new MakeMoveRequest(
                ID_FIRST,
                ID_SECOND,
                "a7",
                "a6"
        );

        when(mockGameRepo.findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND))
                .thenReturn(Optional.of(expectedGame));

        // Mock the static GameServiceHelpers.isMoveAllowed method
        try (MockedStatic<GameServiceHelpers> mockedHelpers = mockStatic(GameServiceHelpers.class)) {
            mockedHelpers.when(() -> GameServiceHelpers.isMoveAllowed(any(), any(), any(), any()))
                    .thenReturn(false);

            GameService gameService = new GameService(mockGameRepo, idService);

            String resultFen = gameService.makeMove(mockMakeMoveRequest);

            verify(mockGameRepo).findGameByPlayerOneIdAndPlayerTwoId(ID_FIRST, ID_SECOND);
            verify(mockGameRepo, never()).save(any(GameModel.class));
            assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", resultFen);
        }
    }

    @Test
    @DirtiesContext
    void makeMove_failed() {
        GameService gameService = new GameService(mockGameRepo, idService);
        assertThrows(NullPointerException.class, () -> gameService.makeMove(null));
        verifyNoInteractions(mockGameRepo);
    }


}
package com.example.backend.controller;

import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.dto.pieceMovement.MakeMoveRequest;
import com.example.backend.model.GameModel;
import com.example.backend.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {
    private static final String URL_BASE = "/api/game";

    private static final String ID_FIRST = "John";
    private static final String ID_SECOND = "Jane";
    private static final String ID_THIRD = "Jack";

    private SecurityMockMvcRequestPostProcessors.OidcLoginRequestPostProcessor mockUser() {
        return oidcLogin().userInfoToken(token -> token
                .claim("login", ID_FIRST));
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;


    @Test
    @DirtiesContext
    void createGame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                              {
                                                "playerOneId": "%s",
                                                "playerTwoId": "%s"
                                              }
                                        """.formatted(ID_FIRST, ID_SECOND)
                        )
                        .with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        List<GameModel> users = gameRepository.findAll();
        assertEquals(1, users.size());
    }

    @Test
    @DirtiesContext
    void createGame_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                              {
                                                "playerOneId": "%s",
                                                "playerTwoId": null
                                              }
                                        """.formatted(ID_FIRST)
                        )
                        .with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        List<GameModel> actualGames = gameRepository.findAll();
        List<GameModel> expectedGames = List.of();
        assertEquals(actualGames, expectedGames);
    }

    @Test
    @DirtiesContext
    void getGame() throws Exception {
        GameModel firstGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_SECOND).build();
        GameModel secondGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_THIRD).build();

        gameRepository.saveAll(List.of(firstGame, secondGame));
        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/getGame/" + firstGame.getPlayerOneId() + "/" + firstGame.getPlayerTwoId()).with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.playerOneId").value(ID_FIRST))
                .andExpect(jsonPath("$.id").value(firstGame.getId()));
    }

    @Test
    @DirtiesContext
    void getGame_fail() throws Exception {
        GameModel firstGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_SECOND).build();
        GameModel secondGame = GameModel.builder().playerOneId(ID_SECOND).playerTwoId(ID_THIRD).build();

        gameRepository.saveAll(List.of(firstGame, secondGame));
        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/getGame/" + ID_FIRST + "/" + ID_THIRD).with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    @DirtiesContext
    void doesGameExist() throws Exception {
        GameModel firstGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_SECOND).build();
        GameModel secondGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_THIRD).build();

        gameRepository.saveAll(List.of(firstGame, secondGame));

        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/doesGameExist/" + firstGame.getPlayerOneId() + "/" + firstGame.getPlayerTwoId()).with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        List<GameModel> games = gameRepository.findAll();
        assertEquals(2, gameRepository.count());
        assertEquals(ID_FIRST, games.get(0).getPlayerOneId());
        assertEquals(ID_SECOND, games.get(0).getPlayerTwoId());
        assertEquals(ID_FIRST, games.get(1).getPlayerOneId());
        assertEquals(ID_THIRD, games.get(1).getPlayerTwoId());
    }

    @Test
    @DirtiesContext
    void doesGameExist_Fail() throws Exception {
        GameModel firstGame = GameModel.builder().playerOneId(ID_FIRST).playerTwoId(ID_SECOND).build();

        gameRepository.save(firstGame);
        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/doesGameExist/" + ID_FIRST + "/" + ID_THIRD).with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(false));

    }
}
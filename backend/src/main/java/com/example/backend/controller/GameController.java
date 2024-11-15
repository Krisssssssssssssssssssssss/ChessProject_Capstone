package com.example.backend.controller;

import com.example.backend.dto.GameRequest;
import com.example.backend.dto.GameResponse;
import com.example.backend.dto.MakeMoveRequest;
import com.example.backend.model.GameModel;
import com.example.backend.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    @PostMapping
    public GameResponse createGame(@RequestBody GameRequest gameRequest) throws Exception {
        GameModel game = gameRequest.toModel();
        return GameResponse.from(gameService.createGame(game));
    }

    @GetMapping("/getGame/{playerOneId}/{playerTwoId}")
    public GameModel getGame(@PathVariable String playerOneId, @PathVariable String playerTwoId) {
        return gameService.getGame(playerOneId, playerTwoId);
    }
    @GetMapping("/doesGameExist/{playerOneId}/{playerTwoId}")
    public boolean doesGameExist (@PathVariable String playerOneId, @PathVariable String playerTwoId) {
        return gameService.doesGameExist(playerOneId, playerTwoId);
    }
    @GetMapping("/move")
    public String makeMove (MakeMoveRequest makeMoveRequest) {
        return gameService.makeMove(makeMoveRequest);
    }
}

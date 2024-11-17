package com.example.backend.dto;

import com.example.backend.model.GameModel;
import lombok.Builder;

@Builder
public record GameResponse(String id, String fenString, String playerOneId, String playerTwoId, String hasTurn) {
    public static GameResponse from(GameModel game) {
        return GameResponse.builder()
                .id(game.getId())
                .fenString(game.getFenString())
                .playerOneId(game.getPlayerOneId())
                .playerTwoId(game.getPlayerTwoId())
                .hasTurn(game.getHasTurn())
                .build();
    }
}

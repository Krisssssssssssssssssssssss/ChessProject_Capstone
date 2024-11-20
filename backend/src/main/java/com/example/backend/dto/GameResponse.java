package com.example.backend.dto;

import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.model.CastlingModel;
import com.example.backend.model.GameModel;
import lombok.Builder;

@Builder
public record GameResponse(String id, String fenString, String playerOneId, String playerTwoId, boolean isWhite,
                           EnPassant enPassant, CastlingModel castlingModel) {
    public static GameResponse from(GameModel game) {
        return GameResponse.builder()
                .id(game.getId())
                .fenString(game.getFenString())
                .playerOneId(game.getPlayerOneId())
                .playerTwoId(game.getPlayerTwoId())
                .isWhite(game.isWhite())
                .enPassant(game.getEnPassant())
                .castlingModel(game.getCastlingModel())
                .build();
    }
}

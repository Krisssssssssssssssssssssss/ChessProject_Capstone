package com.example.backend.dto;

import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.model.GameModel;
import lombok.NonNull;

public record GameRequest(
        @NonNull String playerOneId,
        @NonNull String playerTwoId
) {
    public GameModel toModel() {
        return GameModel.builder()
                .fenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR")
                .playerOneId(playerOneId)
                .playerTwoId(playerTwoId)
                .isWhite(true)
                .enPassant(new EnPassant("", ""))
                .build();
    }
}
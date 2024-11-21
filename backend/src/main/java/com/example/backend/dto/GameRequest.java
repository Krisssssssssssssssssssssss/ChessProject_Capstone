package com.example.backend.dto;

import com.example.backend.dto.piece_movement.EnPassant;
import com.example.backend.model.CastlingModel;
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
                .castlingModel(new CastlingModel(false, false, false, false, false, false, ""))
                .build();
    }
}
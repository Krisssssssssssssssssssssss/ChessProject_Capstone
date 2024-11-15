package com.example.backend.dto;

import com.example.backend.model.GameModel;
import lombok.NonNull;

public record GameRequest(
        @NonNull String playerOneId,
        @NonNull String playerTwoId
) {
    public GameModel toModel() {
        return GameModel.builder()
                .fenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq e3 0 1.")
                .playerOneId(playerOneId)
                .playerTwoId(playerTwoId)
                .build();
    }
}
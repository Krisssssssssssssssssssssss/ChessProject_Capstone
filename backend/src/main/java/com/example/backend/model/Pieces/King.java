package com.example.backend.model.Pieces;

import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.KingService;
import com.example.backend.service.pieceMovement.PawnService;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@With
@Data
public class King {

    public static boolean canMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        return KingService.canMove(board, sourceSquare, targetSquare);
    }
}

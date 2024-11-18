package com.example.backend.model.Pieces;

import com.example.backend.model.GameModel;
import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.PawnService;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@With
@Data
public class Pawn{
    public static boolean canMove(List<List<Tile>> board, String sourceSquare, String targetSquare, GameModel game) {
        return PawnService.canMove(board, sourceSquare, targetSquare, game);
    }
}

package com.example.backend.model.Pieces;

import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.KnightService;
import com.example.backend.service.pieceMovement.PawnService;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@With
@Data
public class Knight {

    public static boolean canMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        return KnightService.canMove(board, sourceSquare, targetSquare);
    }
}

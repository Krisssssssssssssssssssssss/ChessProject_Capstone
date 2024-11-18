package com.example.backend.model.Pieces;

import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.SlidingPieces;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@With
@Data
public class Rook {

    public static boolean canMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        return SlidingPieces.canMove(board, sourceSquare, targetSquare);
    }
}
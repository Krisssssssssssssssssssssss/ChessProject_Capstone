package com.example.backend.model.Pieces;

import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.SlidingPiecesService;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@With
@Data
public class Queen{

    public static boolean canMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        return SlidingPiecesService.canMove(board, sourceSquare, targetSquare);
    }
}

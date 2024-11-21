package com.example.backend.service.pieceMovement;
import com.example.backend.model.Piece;
import com.example.backend.model.Tile;
import java.util.List;

public class QueenService {
    public static boolean canMove(List<List<Tile>> board, Tile sourceTile, Tile targetTile, Piece pieceToMove) {
            if (!RookService.canMove(board, sourceTile, targetTile) && !BishopService.canMove(board, sourceTile, targetTile)) {
                return false;
            }
            return true;
    }
}
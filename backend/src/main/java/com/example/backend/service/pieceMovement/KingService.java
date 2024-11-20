package com.example.backend.service.pieceMovement;

import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.helperMethods.Castling;
import com.example.backend.service.pieceMovement.helperMethods.MajorPiecesHelperMethods;

import java.util.List;

public class KingService {
    public static boolean canMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        Tile sourceTile = null;
        Tile targetTile = null;

        //Getting the sourceTile and the pieceToMove
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(sourceSquare)) {
                    sourceTile = tile;
                }
            }
        }
        //TargetTile
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(targetSquare)) {
                    targetTile = tile;
                }
            }
        }
        int howManyTilesMoved = MajorPiecesHelperMethods.howManyTilesMoved(sourceTile, targetTile);
        if (howManyTilesMoved > 1 && !Castling.kingIsCastling) {
            return false;
        }
        return true;
    }
}
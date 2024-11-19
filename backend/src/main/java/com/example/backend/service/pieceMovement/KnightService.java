package com.example.backend.service.pieceMovement;

import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.helperMethods.MajorPiecesHelperMethods;

import java.util.List;

public class KnightService {

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
        int xSum = MajorPiecesHelperMethods.howManyFieldsMoved(sourceTile.getX(), targetTile.getX());
        int ySum = MajorPiecesHelperMethods.howManyFieldsMoved(sourceTile.getY(), targetTile.getY());
        //Should not move more than 2 squares in any direction
        if (!((xSum == 2 && ySum == 1) || (xSum == 1 && ySum == 2))) {
            return false;
        }
        return true;
    }
}

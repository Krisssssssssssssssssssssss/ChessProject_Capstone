package com.example.backend.service.pieceMovement;

import com.example.backend.model.Piece;
import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.helperMethods.MajorPiecesHelperMethods;

public class KnightService {

    public static boolean canMove(Tile sourceTile, Tile targetTile, Piece pieceToMove) {

        int xSum = MajorPiecesHelperMethods.howManyFieldsMovedIndividualDirection(sourceTile.getX(), targetTile.getX());
        int ySum = MajorPiecesHelperMethods.howManyFieldsMovedIndividualDirection(sourceTile.getY(), targetTile.getY());
        //Should move in L shape
        if (!((xSum == 2 && ySum == 1) || (xSum == 1 && ySum == 2))) {
            return false;
        }
        return true;
    }
}

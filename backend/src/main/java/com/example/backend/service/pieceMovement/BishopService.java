package com.example.backend.service.pieceMovement;

import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.helperMethods.MajorPiecesHelperMethods;

import java.util.List;

public class BishopService {
    public static boolean canMove(List<List<Tile>> board, Tile sourceTile, Tile targetTile) {
        int xSum = MajorPiecesHelperMethods.howManyFieldsMovedIndividualDirection(sourceTile.getX(), targetTile.getX());
        int ySum = MajorPiecesHelperMethods.howManyFieldsMovedIndividualDirection(sourceTile.getY(), targetTile.getY());
        if (xSum != ySum) {
            return false;
        }
        boolean isJumpingOver = MajorPiecesHelperMethods.isJumpingOverDiagonally(board, sourceTile, targetTile, xSum);
        return isJumpingOver;
    }
}

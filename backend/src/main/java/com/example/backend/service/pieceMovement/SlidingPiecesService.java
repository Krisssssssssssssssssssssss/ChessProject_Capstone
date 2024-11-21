package com.example.backend.service.pieceMovement;

import com.example.backend.model.Piece;
import com.example.backend.model.Tile;
import com.example.backend.service.RookServices;
import com.example.backend.service.pieceMovement.helperMethods.MajorPiecesHelperMethods;

import java.util.List;


public class SlidingPiecesService {
    public static boolean canMove(List<List<Tile>> board, Tile sourceTile, Tile targetTile, Piece pieceToMove) {

        boolean canMove;
        switch (pieceToMove.getType().toLowerCase()) {
            case "b" -> canMove = isAllowedBishop(board, sourceTile, targetTile);
            case "q" -> canMove = isAllowedQueen(board, sourceTile, targetTile, pieceToMove);
            default -> canMove = false;
        }
        return canMove;
    }


    public static boolean isAllowedBishop(List<List<Tile>> board, Tile sourceTile, Tile targetTile) {
        int xSum = MajorPiecesHelperMethods.howManyFieldsMovedIndividualDirection(sourceTile.getX(), targetTile.getX());
        int ySum = MajorPiecesHelperMethods.howManyFieldsMovedIndividualDirection(sourceTile.getY(), targetTile.getY());
        if (xSum != ySum) {
            return false;
        }
        boolean isJumpingOver = MajorPiecesHelperMethods.isJumpingOverDiagonally(board, sourceTile, targetTile, xSum);
        return isJumpingOver;
    }

    private static boolean isAllowedQueen(List<List<Tile>> board, Tile sourceTile, Tile targetTile, Piece pieceToMove) {
        if (!RookServices.canMove(board, sourceTile, targetTile) && !isAllowedBishop(board, sourceTile, targetTile)) {
            return false;
        }
        return true;
    }
}
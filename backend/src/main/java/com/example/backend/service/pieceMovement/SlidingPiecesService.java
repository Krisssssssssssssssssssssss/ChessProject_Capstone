package com.example.backend.service.pieceMovement;

import com.example.backend.constants.StringConstants;
import com.example.backend.model.Pieces.*;
import com.example.backend.model.Tile;
import com.example.backend.service.GameService;
import com.example.backend.service.pieceMovement.helperMethods.MajorPiecesHelperMethods;

import java.util.List;


public class SlidingPiecesService {
    public static boolean canMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {

        Tile sourceTile = null;
        Tile targetTile = null;
        Piece pieceToMove = null;

        //Getting the sourceTile and the pieceToMove
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(sourceSquare)) {
                    pieceToMove = tile.getPiece();
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
        boolean canMove = false;
        switch (pieceToMove.getType().toLowerCase()) {
            case "r" -> canMove = isAllowedRook(board, sourceTile, targetTile);
            case "b" -> canMove = isAllowedBishop(board, sourceTile, targetTile);
            case "q" -> canMove = isAllowedQueen(board, sourceTile, targetTile, pieceToMove);
            default -> throw new IllegalArgumentException("Unknown piece type: " + pieceToMove.getType());
        }
        return canMove;
    }

    public static boolean isAllowedRook(List<List<Tile>> board, Tile sourceTile, Tile targetTile) {
        if ((sourceTile.getY() != targetTile.getY() && (sourceTile.getX() != targetTile.getX()))) {
            return false;
        }

        int xySumBigger = MajorPiecesHelperMethods.howManyTilesMoved(sourceTile, targetTile);

        boolean isNotJumpingOver = MajorPiecesHelperMethods.isNotJumpingOver(board, sourceTile, targetTile, xySumBigger);
        if (isNotJumpingOver) {
            if (sourceTile.getPiece().getColor() == "w") {
                if (sourceTile.getName().equals(StringConstants.ROOK_A1.getCode())) {
                    GameService.localCastling.setRookA1Moved(true);
                }
                if (sourceTile.getName().equals(StringConstants.ROOK_H1.getCode())) {
                    GameService.localCastling.setRookH1Moved(true);
                }
            }
            if (sourceTile.getPiece().getColor() == "b") {
                if (sourceTile.getName().equals(StringConstants.ROOK_A8.getCode())) {
                    GameService.localCastling.setRookA8Moved(true);
                }
                if (sourceTile.getName().equals(StringConstants.ROOK_H8.getCode())) {
                    GameService.localCastling.setRookH8Moved(true);
                }
            }
        }
        return isNotJumpingOver;
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
        if (!isAllowedRook(board, sourceTile, targetTile) && !isAllowedBishop(board, sourceTile, targetTile)) {
            return false;
        }
        return true;
    }
}
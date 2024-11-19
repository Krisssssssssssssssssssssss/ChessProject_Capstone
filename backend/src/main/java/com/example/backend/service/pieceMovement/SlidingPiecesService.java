package com.example.backend.service.pieceMovement;

import com.example.backend.model.Pieces.*;
import com.example.backend.model.Tile;
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
            case "r" -> canMove = isRestrictedRook(board, sourceTile, targetTile, pieceToMove);
            case "b" -> canMove = isRestrictedBishop(board, sourceTile, targetTile, pieceToMove);
            case "q" -> canMove = isRestrictedQueen(board, sourceTile, targetTile, pieceToMove);
            default -> throw new IllegalArgumentException("Unknown piece type: " + pieceToMove.getType());
        }
        return canMove;
    }

    private static boolean isRestrictedRook(List<List<Tile>> board, Tile sourceTile, Tile targetTile, Piece pieceToMove) {
        if ((sourceTile.getY() != targetTile.getY() && (sourceTile.getX() != targetTile.getX()))) {
            return false;
        }
        int xSum = howManyFieldsStraightLine(sourceTile.getX(), targetTile.getX());
        int ySum = howManyFieldsStraightLine(sourceTile.getY(), targetTile.getY());
        int xySumBigger;
        if (xSum > ySum) {
            xySumBigger = xSum;
        }
        else {xySumBigger = ySum;}
        boolean isJumpingOver = MajorPiecesHelperMethods.isJumpingOverStraightLine(board, sourceTile, targetTile, xySumBigger);
        return isJumpingOver;
    }

    private static boolean isRestrictedBishop(List<List<Tile>> board, Tile sourceTile, Tile targetTile, Piece pieceToMove) {
        int xSum = howManyFieldsDiagonally(sourceTile.getX(), targetTile.getX());
        int ySum = howManyFieldsDiagonally(sourceTile.getY(), targetTile.getY());
        if (xSum != ySum) {
            return false;
        }
        boolean isJumpingOver = MajorPiecesHelperMethods.isJumpingOverDiagonally(board, sourceTile, targetTile, xSum);
        return isJumpingOver;
    }

    private static boolean isRestrictedQueen(List<List<Tile>> board, Tile sourceTile, Tile targetTile, Piece pieceToMove) {
        if (!isRestrictedRook(board, sourceTile, targetTile, pieceToMove) && !isRestrictedBishop(board, sourceTile, targetTile, pieceToMove)) {
            return false;
        }
        return true;
    }

    private static int howManyFieldsDiagonally(int starting, int ending) {
        if (starting > ending) {
            return starting - ending;
        } else {
            return ending - starting;
        }
    }
    private static int howManyFieldsStraightLine(int starting, int ending) {
        if (starting > ending) {
            return starting - ending;
        } else {
            return ending - starting;
        }
    }
}

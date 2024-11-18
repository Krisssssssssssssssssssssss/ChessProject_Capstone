package com.example.backend.service.pieceMovement;

import com.example.backend.model.GameModel;
import com.example.backend.model.Pieces.*;
import com.example.backend.model.Tile;

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
        return true;
    }

    private static boolean isRestrictedBishop(List<List<Tile>> board, Tile sourceTile, Tile targetTile, Piece pieceToMove) {
        int xSum = howManyFieldsDiagonally(sourceTile.getX(), targetTile.getX());
        int ySum = howManyFieldsDiagonally(sourceTile.getY(), targetTile.getY());
        if (xSum != ySum) {
            return false;
        }
        return true;
    }

    private static boolean isRestrictedQueen(List<List<Tile>> board, Tile sourceTile, Tile targetTile, Piece pieceToMove) {
        if (isRestrictedRook(board, sourceTile, targetTile, pieceToMove) || isRestrictedBishop(board, sourceTile, targetTile, pieceToMove)) {
            return true;
        }
        return false;
    }

    private static int howManyFieldsDiagonally(int starting, int ending) {
        if (starting > ending) {
            return starting - ending;
        } else {
            return ending - starting;
        }
    }

}

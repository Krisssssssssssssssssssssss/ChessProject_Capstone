package com.example.backend.service.pieceMovement;

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
        int xSum = howManyFieldsStraightLine(sourceTile.getX(), targetTile.getX());
        int ySum = howManyFieldsStraightLine(sourceTile.getY(), targetTile.getY());
        int xySumBigger;
        if (xSum > ySum) {
            xySumBigger = xSum;
        }
        else {xySumBigger = ySum;}
        boolean isJumpingOver = isJumpingOverStraightLine(board, sourceTile, targetTile, xySumBigger);
        return isJumpingOver;
    }

    private static boolean isRestrictedBishop(List<List<Tile>> board, Tile sourceTile, Tile targetTile, Piece pieceToMove) {
        int xSum = howManyFieldsDiagonally(sourceTile.getX(), targetTile.getX());
        int ySum = howManyFieldsDiagonally(sourceTile.getY(), targetTile.getY());
        if (xSum != ySum) {
            return false;
        }
        boolean isJumpingOver = isJumpingOverDiagonally(board, sourceTile, targetTile, xSum);
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

    private static boolean isJumpingOverStraightLine(List<List<Tile>> board, Tile sourceTile, Tile targetTile, int xySum) {
        boolean isXchanging = false;
        boolean isXincreasing = false;
        boolean isYincreasing = false;

        if (sourceTile.getX() == targetTile.getX()) {
            if (sourceTile.getY() < targetTile.getY()) {
                isYincreasing = true;
            }
        } else {
            isXchanging = true;
            if (sourceTile.getX() < targetTile.getX()) {
                isXincreasing = true;
            }
        }

        //We always calculate from the source
        if (isXchanging) {
            if (isXincreasing) {
                for (int i = 1; i < xySum; i++) {
                    for (List<Tile> row : board) {
                        for (Tile tile : row) {
                            if (tile.getX() == sourceTile.getX() + i && tile.getY() == sourceTile.getY()) {
                                if (tile.isOccupied()) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            if (!isXincreasing) {
                for (int i = 1; i < xySum; i++) {
                    for (List<Tile> row : board) {
                        for (Tile tile : row) {
                            if (tile.getX() == sourceTile.getX() - i && tile.getY() == sourceTile.getY()) {
                                if (tile.isOccupied()) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            if (isYincreasing) {
                for (int i = 1; i < xySum; i++) {
                    for (List<Tile> row : board) {
                        for (Tile tile : row) {
                            if (tile.getY() == sourceTile.getY() + i && tile.getX() == sourceTile.getX()) {
                                if (tile.isOccupied()) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            if (!isYincreasing) {
                for (int i = 1; i < xySum; i++) {
                    for (List<Tile> row : board) {
                        for (Tile tile : row) {
                            if (tile.getY() == sourceTile.getY() - i && tile.getX() == sourceTile.getX()) {
                                if (tile.isOccupied()) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    private static boolean isJumpingOverDiagonally(List<List<Tile>> board, Tile sourceTile, Tile targetTile, int xySum) {
        boolean isXincreasing;
        boolean isYincreasing;
        if (sourceTile.getX() < targetTile.getX()) {
            isXincreasing = true;
        } else {
            isXincreasing = false;
        }
        if (sourceTile.getY() < targetTile.getY()) {
            isYincreasing = true;
        } else {
            isYincreasing = false;
        }
        //We always calculate from the source
        if (isXincreasing && isYincreasing) {
            for (int i = 1; i < xySum; i++) {
                for (List<Tile> row : board) {
                    for (Tile tile : row) {
                        if (tile.getX() == sourceTile.getX() + i && tile.getY() == sourceTile.getY() + i) {
                            if (tile.isOccupied()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        if (!isXincreasing && !isYincreasing) {
            for (int i = 1; i < xySum; i++) {
                for (List<Tile> row : board) {
                    for (Tile tile : row) {
                        if (tile.getX() == sourceTile.getX() - i && tile.getY() == sourceTile.getY() - i) {
                            if (tile.isOccupied()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        if (isXincreasing && !isYincreasing) {
            for (int i = 1; i < xySum; i++) {
                for (List<Tile> row : board) {
                    for (Tile tile : row) {
                        if (tile.getX() == sourceTile.getX() + i && tile.getY() == sourceTile.getY() - i) {
                            if (tile.isOccupied()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        if (!isXincreasing && isYincreasing) {
            for (int i = 1; i < xySum; i++) {
                for (List<Tile> row : board) {
                    for (Tile tile : row) {
                        if (tile.getX() == sourceTile.getX() - i && tile.getY() == sourceTile.getY() + i) {
                            if (tile.isOccupied()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}

package com.example.backend.service.pieceMovement.helperMethods;

import com.example.backend.model.Tile;

import java.util.List;

public class MajorPiecesHelperMethods {
    public static boolean isJumpingOverStraightLine(List<List<Tile>> board, Tile sourceTile, Tile targetTile, int xySum) {
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
    public static boolean isJumpingOverDiagonally(List<List<Tile>> board, Tile sourceTile, Tile targetTile, int xySum) {
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
    public static int howManyFieldsMoved(int starting, int ending) {
        if (starting > ending) {
            return starting - ending;
        } else {
            return ending - starting;
        }
    }
}

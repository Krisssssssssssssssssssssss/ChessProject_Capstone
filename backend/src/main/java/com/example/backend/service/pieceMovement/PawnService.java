package com.example.backend.service.pieceMovement;

import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.model.GameModel;
import com.example.backend.model.Pieces.Piece;
import com.example.backend.model.Tile;

import java.util.List;
public class PawnService {
    public static boolean isMovingTwoTiles;
    public static boolean isEnPassant;
    public static EnPassant enPassant;
    public static boolean canMove(List<List<Tile>> board, String sourceSquare, String targetSquare, GameModel game) {
        Tile sourceTile = null;
        Tile targetTile = null;
        Piece pieceToMove = null;
        enPassant = game.getEnPassant();
        isMovingTwoTiles = false;
        isEnPassant = false;

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

        return isRestricted(sourceTile, targetTile, pieceToMove, game);
    }

    private static boolean isRestricted(Tile sourceTile, Tile targetTile, Piece pieceToMove, GameModel game) {
        int howManyYFieldsMoved = 0;
        //can't kill forwards
        if (sourceTile.getX() == targetTile.getX() && targetTile.isOccupied()) {
            return false;
        }

        //Restrictions specific for white
        if (pieceToMove.getColor().equals("w")) {
            //No backwards or sideways movement
            if (sourceTile.getY() <= targetTile.getY()) {
                return false;
            }
            //Only one field at a time besides the beginning
            //NOTE: the Y axis is reverted because the FEN starts with black pieces
            howManyYFieldsMoved = sourceTile.getY() - targetTile.getY();
            if ((howManyYFieldsMoved > 2) || ((howManyYFieldsMoved == 2) && sourceTile.getY() != 6)) {
                return false;
            }
        }
        //Restrictions specific for black
        if (pieceToMove.getColor().equals("b")) {
            //No backwards or sideways movement
            if (sourceTile.getY() >= targetTile.getY()) {
                return false;
            }
            //Only one field at a time besides the beginning
            //NOTE: the Y axis is reverted because the FEN starts with black pieces
            howManyYFieldsMoved = targetTile.getY() - sourceTile.getY();
            if ((howManyYFieldsMoved > 2) || ((howManyYFieldsMoved == 2) && sourceTile.getY() != 1)) {
                return false;
            }
        }
        //Diagonal kill
        if (sourceTile.getX() != targetTile.getX()) {
            int howManyXFieldsMoved = 0;
            if (sourceTile.getX() > targetTile.getX()) {
                howManyXFieldsMoved = sourceTile.getX() - targetTile.getX();
            } else {
                howManyXFieldsMoved = targetTile.getX() - sourceTile.getX();
            }

            if (howManyYFieldsMoved > 1) {
                return false;
            }
            if (howManyXFieldsMoved > 1) {
                return false;
            }
            //Check if enPassant was set on previous move
            if (!targetTile.isOccupied()) {
                if (game.getEnPassant().enPassantEmptyField().equals("")) {
                    return false;
                } else {
                    if (!game.getEnPassant().enPassantEmptyField().equals(targetTile.getName())) {
                        return false;
                    }
                    else {
                        isEnPassant = true;
                    }
                }
            }
        }

        //Setting up enPassant if moving 2 squares
        isMovingTwoTiles = howManyYFieldsMoved == 2;
        if (isMovingTwoTiles) {
            if (pieceToMove.getColor().equals("w")) {
                enPassant = new EnPassant(getTileName(sourceTile.getX(), targetTile.getY() + 1), targetTile.getName());

            } else if (pieceToMove.getColor().equals("b")) {
                enPassant = new EnPassant(getTileName(sourceTile.getX(), targetTile.getY() - 1), targetTile.getName());
            }
        }
        else {
            if (!isEnPassant) {
                enPassant = new EnPassant("", "");
            }
        }
        return true;
    }


    private static String getTileName(int x, int y) {
        char file = (char) ('a' + x);
        int rank = 8 - y;
        return "" + file + rank;
    }
}

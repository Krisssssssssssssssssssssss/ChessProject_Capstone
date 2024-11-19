package com.example.backend.service.pieceMovement.helperMethods;

import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.model.GameModel;
import com.example.backend.model.Pieces.Piece;
import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.PawnService;

public class PawnHelperMethods {
    public static boolean isRestricted(Tile sourceTile, Tile targetTile, Piece pieceToMove, GameModel game) {
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
                        PawnService.isEnPassant = true;
                    }
                }
            }
        }

        //Setting up enPassant if moving 2 squares
        PawnService.isMovingTwoTiles = howManyYFieldsMoved == 2;
        if (PawnService.isMovingTwoTiles) {
            if (pieceToMove.getColor().equals("w")) {
                PawnService.enPassant = new EnPassant(getTileName(sourceTile.getX(), targetTile.getY() + 1), targetTile.getName());

            } else if (pieceToMove.getColor().equals("b")) {
                PawnService.enPassant = new EnPassant(getTileName(sourceTile.getX(), targetTile.getY() - 1), targetTile.getName());
            }
        }
        else {
            if (!PawnService.isEnPassant) {
                PawnService.enPassant = new EnPassant("", "");
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

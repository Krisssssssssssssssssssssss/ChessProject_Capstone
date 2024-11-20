package com.example.backend.service.pieceMovement;

import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.model.GameModel;
import com.example.backend.model.Piece;
import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.helperMethods.PawnHelperMethods;

public class PawnService {
    public static boolean isMovingTwoTiles;
    public static boolean isEnPassant;
    public static EnPassant enPassant;

    public static boolean canMove(Tile sourceTile, Tile targetTile, Piece pieceToMove, GameModel game) {

        enPassant = game.getEnPassant();
        isMovingTwoTiles = false;
        isEnPassant = false;

        return PawnHelperMethods.isRestricted(sourceTile, targetTile, pieceToMove, game);
    }
}

package com.example.backend.service.pieceMovement;

import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.model.GameModel;
import com.example.backend.model.Pieces.Piece;
import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.helperMethods.PawnHelperMethods;

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
        return PawnHelperMethods.isRestricted(sourceTile, targetTile, pieceToMove, game);
    }
}

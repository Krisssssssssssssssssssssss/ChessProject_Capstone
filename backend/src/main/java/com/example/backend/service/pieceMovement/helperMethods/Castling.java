package com.example.backend.service.pieceMovement.helperMethods;

import com.example.backend.constants.StringConstants;
import com.example.backend.model.CastlingModel;
import com.example.backend.model.GameModel;
import com.example.backend.model.Tile;

import java.util.List;

public class Castling {
    public static boolean kingIsCastling = false;
    public static CastlingModel localCastling;

    public static boolean canKingCastle(List<List<Tile>> board, Tile sourceTile, Tile targetTile, GameModel game) {

        int tilesMoved = MajorPiecesHelperMethods.howManyTilesMoved(sourceTile, targetTile);
        boolean isNotJumpingOver = MajorPiecesHelperMethods.isNotJumpingOver(board, sourceTile, targetTile, tilesMoved);
        if (!isNotJumpingOver) {
            return false;
        }
        if (sourceTile.getPiece().getColor() == StringConstants.WHITE.getCode()) {
            if (localCastling.isWhiteKingMoved()) {
                return false;
            }
            if (targetTile.getName().equals(StringConstants.ROOK_A1.getCode())) {
                if (localCastling.isRookA1Moved()) {
                    return false;
                }
                localCastling.setCastlingActivity(StringConstants.ROOK_A1.getCode());
            }

            if (targetTile.getName().equals(StringConstants.ROOK_H1.getCode())) {
                if (localCastling.isRookH1Moved()) {
                    return false;
                }
                localCastling.setCastlingActivity(StringConstants.ROOK_H1.getCode());
            }
            localCastling.setWhiteKingMoved(true);
        }
        if (sourceTile.getPiece().getColor() == StringConstants.BLACK.getCode()) {
            if (localCastling.isBlackKingMoved()) {
                return false;
            }
            if (targetTile.getName().equals(StringConstants.ROOK_A8.getCode())) {
                if (localCastling.isRookA8Moved()){
                    return false;
                }
                localCastling.setCastlingActivity(StringConstants.ROOK_A8.getCode());
            }
            if (targetTile.getName().equals(StringConstants.ROOK_H8.getCode())) {
                if (localCastling.isRookH8Moved()) {
                    return false;
                }
                localCastling.setCastlingActivity(StringConstants.ROOK_H8.getCode());
            }
            localCastling.setBlackKingMoved(true);
        }
        kingIsCastling = true;
        return true;
    }
}

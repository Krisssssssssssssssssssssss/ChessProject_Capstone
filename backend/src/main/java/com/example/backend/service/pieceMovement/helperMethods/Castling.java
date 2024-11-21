package com.example.backend.service.pieceMovement.helperMethods;

import com.example.backend.constants.StringConstants;
import com.example.backend.dto.piece_movement.CastleResponse;
import com.example.backend.model.GameModel;
import com.example.backend.model.Tile;

import java.util.List;

public class Castling {
    //Also SonarCloud suggestion to protect against instantiating
    private Castling() {
        throw new UnsupportedOperationException("Castling is a utility class and cannot be instantiated.");
    }
    public static CastleResponse canKingCastle(List<List<Tile>> board, Tile sourceTile, Tile targetTile, GameModel game) {
        CastleResponse castleResponse = new CastleResponse(false, game.getCastlingModel());
        int tilesMoved = MajorPiecesHelperMethods.howManyTilesMoved(sourceTile, targetTile);
        boolean isNotJumpingOver = MajorPiecesHelperMethods.isNotJumpingOver(board, sourceTile, targetTile, tilesMoved);
        if (!isNotJumpingOver) {
            castleResponse.setKingCanCastle(false);
            castleResponse.setCastlingModel(game.getCastlingModel());
            return castleResponse;
        }
        if (sourceTile.getPiece().getColor() == StringConstants.WHITE.getCode() && !game.getCastlingModel().isWhiteKingMoved()) {
            if (castleResponse.getCastlingModel().isWhiteKingMoved()) {
                castleResponse.setKingCanCastle(false);
                castleResponse.setCastlingModel(game.getCastlingModel());
                return castleResponse;
            }
            if (targetTile.getName().equals(StringConstants.ROOK_A1.getCode())) {
                if (castleResponse.getCastlingModel().isRookA1Moved()) {
                    castleResponse.setKingCanCastle(false);
                    castleResponse.setCastlingModel(game.getCastlingModel());
                    return castleResponse;
                }
                castleResponse.getCastlingModel().setCastlingActivity(StringConstants.ROOK_A1.getCode());
            }

            if (targetTile.getName().equals(StringConstants.ROOK_H1.getCode())) {
                if (castleResponse.getCastlingModel().isRookH1Moved()) {
                    castleResponse.setKingCanCastle(false);
                    castleResponse.setCastlingModel(game.getCastlingModel());
                    return castleResponse;
                }
                castleResponse.getCastlingModel().setCastlingActivity(StringConstants.ROOK_H1.getCode());
            }
            castleResponse.getCastlingModel().setWhiteKingMoved(true);
        }
        if (sourceTile.getPiece().getColor() == StringConstants.BLACK.getCode()) {
            if (castleResponse.getCastlingModel().isBlackKingMoved()) {
                castleResponse.setKingCanCastle(false);
                castleResponse.setCastlingModel(game.getCastlingModel());
                return castleResponse;
            }
            if (targetTile.getName().equals(StringConstants.ROOK_A8.getCode())) {
                if (castleResponse.getCastlingModel().isRookA8Moved()){
                    castleResponse.setKingCanCastle(false);
                    castleResponse.setCastlingModel(game.getCastlingModel());
                    return castleResponse;
                }
                castleResponse.getCastlingModel().setCastlingActivity(StringConstants.ROOK_A8.getCode());
            }
            if (targetTile.getName().equals(StringConstants.ROOK_H8.getCode())) {
                if (castleResponse.getCastlingModel().isRookH8Moved()) {
                    castleResponse.setKingCanCastle(false);
                    castleResponse.setCastlingModel(game.getCastlingModel());
                    return castleResponse;
                }
                castleResponse.getCastlingModel().setCastlingActivity(StringConstants.ROOK_H8.getCode());
            }
            castleResponse.getCastlingModel().setBlackKingMoved(true);
        }
        castleResponse.setKingCanCastle(true);
        return castleResponse;
    }
}

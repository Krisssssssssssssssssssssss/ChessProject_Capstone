package com.example.backend.service.pieceMovement.helperMethods;

import com.example.backend.constants.StringConstants;
import com.example.backend.dto.piece_movement.CastleResponse;
import com.example.backend.model.CastlingModel;
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

        if (!canMoveWithoutJumpingOver(board, sourceTile, targetTile)) {
            return castleResponse;
        }
        String pieceColor = sourceTile.getPiece().getColor();
        if (pieceColor.equals(StringConstants.WHITE.getCode())) {
            return processWhiteKingCastle(targetTile, castleResponse);
        } else if (pieceColor.equals(StringConstants.BLACK.getCode())) {
            return processBlackKingCastle(targetTile, castleResponse);
        }

        return castleResponse;

    }

    private static boolean canMoveWithoutJumpingOver(List<List<Tile>> board, Tile sourceTile, Tile targetTile) {
        int tilesMoved = MajorPiecesHelperMethods.howManyTilesMoved(sourceTile, targetTile);
        return MajorPiecesHelperMethods.isNotJumpingOver(board, sourceTile, targetTile, tilesMoved);
    }

    private static CastleResponse processWhiteKingCastle(Tile targetTile, CastleResponse castleResponse) {
        CastlingModel castlingModel = castleResponse.getCastlingModel();
        if (castlingModel.isWhiteKingMoved()) {
            return castleResponse;
        }

        if (updateCastlingWhite(targetTile, castlingModel, StringConstants.ROOK_A1.getCode(), StringConstants.ROOK_H1.getCode())) {
            castlingModel.setWhiteKingMoved(true);
            castleResponse.setKingCanCastle(true);
        }

        return castleResponse;
    }

    private static CastleResponse processBlackKingCastle(Tile targetTile, CastleResponse castleResponse) {
        CastlingModel castlingModel = castleResponse.getCastlingModel();
        if (castlingModel.isBlackKingMoved()) {
            return castleResponse;
        }

        if (updateCastlingBlack(targetTile, castlingModel, StringConstants.ROOK_A8.getCode(), StringConstants.ROOK_H8.getCode())) {
            castlingModel.setBlackKingMoved(true);
            castleResponse.setKingCanCastle(true);
        }

        return castleResponse;
    }

    private static boolean updateCastlingWhite(Tile targetTile, CastlingModel castlingModel, String rookA, String rookH) {
        if (targetTile.getName().equals(rookA) && !castlingModel.isRookA1Moved()) {
            castlingModel.setCastlingActivity(rookA);
            return true;
        }
        if (targetTile.getName().equals(rookH) && !castlingModel.isRookH1Moved()) {
            castlingModel.setCastlingActivity(rookH);
            return true;
        }
        return false;
    }

    private static boolean updateCastlingBlack(Tile targetTile, CastlingModel castlingModel, String rookA, String rookH) {
        if (targetTile.getName().equals(rookA) && !castlingModel.isRookA8Moved()) {
            castlingModel.setCastlingActivity(rookA);
            return true;
        }
        if (targetTile.getName().equals(rookH) && !castlingModel.isRookH8Moved()) {
            castlingModel.setCastlingActivity(rookH);
            return true;
        }
        return false;
    }
}

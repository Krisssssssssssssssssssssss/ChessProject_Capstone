package com.example.backend.service.pieceMovement;

import com.example.backend.constants.StringConstants;
import com.example.backend.model.Tile;
import com.example.backend.service.GameService;
import com.example.backend.service.pieceMovement.helperMethods.MajorPiecesHelperMethods;

import java.util.List;

public class RookService {
    public static boolean canMove(List<List<Tile>> board, Tile sourceTile, Tile targetTile) {
        if (!isInSameRowOrColumn(sourceTile, targetTile)) {
            return false;
        }

        int xySumBigger = MajorPiecesHelperMethods.howManyTilesMoved(sourceTile, targetTile);
        boolean isNotJumpingOver = MajorPiecesHelperMethods.isNotJumpingOver(board, sourceTile, targetTile, xySumBigger);

        if (isNotJumpingOver) {
            updateCastlingState(sourceTile);
        }
        return isNotJumpingOver;
    }
    private static boolean isInSameRowOrColumn(Tile sourceTile, Tile targetTile) {
        return sourceTile.getY() == targetTile.getY() || sourceTile.getX() == targetTile.getX();
    }
    private static void updateCastlingState(Tile sourceTile) {
        String pieceColor = sourceTile.getPiece().getColor();
        String tileName = sourceTile.getName();

        //Only set them as moved if it's their first move
        if (pieceColor.equals(StringConstants.WHITE.getCode())) {
            if (tileName.equals(StringConstants.ROOK_A1.getCode())) {
                GameService.localCastling.setRookA1Moved(true);
            } else if (tileName.equals(StringConstants.ROOK_H1.getCode())) {
                GameService.localCastling.setRookH1Moved(true);
            }
        } else if (pieceColor.equals(StringConstants.BLACK.getCode())) {
            if (tileName.equals(StringConstants.ROOK_A8.getCode())) {
                GameService.localCastling.setRookA8Moved(true);
            } else if (tileName.equals(StringConstants.ROOK_H8.getCode())) {
                GameService.localCastling.setRookH8Moved(true);
            }
        }
    }
}


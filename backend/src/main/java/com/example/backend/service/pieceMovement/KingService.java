package com.example.backend.service.pieceMovement;

import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.helperMethods.MajorPiecesHelperMethods;

public class KingService {
    public static boolean canMove(Tile sourceTile, Tile targetTile) {
        int howManyTilesMoved = MajorPiecesHelperMethods.howManyTilesMoved(sourceTile, targetTile);
        return howManyTilesMoved == 1;
    }
}
package com.example.backend.service;

import com.example.backend.constants.StringConstants;
import com.example.backend.dto.pieceMovement.CastleResponse;
import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.dto.pieceMovement.MakeMoveRequest;
import com.example.backend.model.GameModel;
import com.example.backend.model.Piece;
import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.KingService;
import com.example.backend.service.pieceMovement.KnightService;
import com.example.backend.service.pieceMovement.PawnService;
import com.example.backend.service.pieceMovement.SlidingPiecesService;
import com.example.backend.service.pieceMovement.helperMethods.Castling;

import java.util.List;

public class GameServiceHelpers {


    public static GameModel updateGameState(GameModel game, List<List<Tile>> gameBoard, MakeMoveRequest makeMoveRequest) {
        gameBoard = executeTheMove(gameBoard, makeMoveRequest.sourceSquare(), makeMoveRequest.targetSquare());
        String fenString = FenConverter.toFen(gameBoard);
        game.setFenString(fenString);
        game.setWhite(!game.isWhite());
        game.setEnPassant(PawnService.enPassant);
        game.setCastlingModel(GameService.localCastling);
        return game;
    }

    public static void resetTemporaryStates() {
        PawnService.enPassant = new EnPassant("", "");
        GameService.kingIsCastling = false;
    }


    public static boolean isMoveAllowed(List<List<Tile>> board, String sourceSquare, String targetSquare, GameModel game) {

        Tile sourceTile = findTileAt(board, sourceSquare);
        Tile targetTile = findTileAt(board, targetSquare);
        Piece pieceToMove = sourceTile.getPiece();

        if (!isPlayerTurn(pieceToMove, game)) {
            return false;
        }
        if (isIllegalCapture(sourceTile, targetTile)) {
            return false;
        }

        return canPieceMove(pieceToMove, board, sourceTile, targetTile, game);

    }

    public static boolean canPieceMove(Piece pieceToMove, List<List<Tile>> board, Tile sourceTile, Tile targetTile, GameModel game) {
        boolean canMove;
        if (!pieceToMove.getType().toLowerCase().equals(StringConstants.PAWN.getCode())) {
            PawnService.enPassant = new EnPassant("", "");
        }
        if (sourceTile.getPiece().isKing()
                && targetTile.getPiece().getType().equalsIgnoreCase("r")
        ) {
            if (isThatKingAlreadyCastled(pieceToMove, game)) {
                return false;
            }
            CastleResponse castleResponse = Castling.canKingCastle(board, sourceTile, targetTile, game);
            if (castleResponse.isKingCanCastle()) {
                GameService.kingIsCastling = true;
                GameService.localCastling = castleResponse.getCastlingModel();
                return true;
            }
        }

        switch (pieceToMove.getType().toLowerCase()) {
            case "p" -> canMove = PawnService.canMove(sourceTile, targetTile, pieceToMove, game);
            case "n" -> canMove = KnightService.canMove(sourceTile, targetTile, pieceToMove);
            case "r", "q", "b" -> canMove = SlidingPiecesService.canMove(board, sourceTile, targetTile, pieceToMove);
            case "k" -> canMove = KingService.canMove(sourceTile, targetTile);
            default -> canMove = false;
        }
        return canMove;
    }

    public static boolean isThatKingAlreadyCastled(Piece pieceToMove, GameModel game) {
        if (pieceToMove.getColor().equals(StringConstants.WHITE.getCode()) && game.getCastlingModel().isWhiteKingMoved()) {
            return true;
        } else if (pieceToMove.getColor().equals(StringConstants.BLACK.getCode()) && game.getCastlingModel().isBlackKingMoved()) {
            return true;
        }
        return false;
    }

    public static boolean isIllegalCapture(Tile sourceTile, Tile targetTile) {
        return sourceTile.getPiece().getColor().equals(targetTile.getPiece().getColor()) &&
                !(sourceTile.getPiece().isKing() && targetTile.getPiece().getType().equalsIgnoreCase("r"));
    }

    public static boolean isPlayerTurn(Piece piece, GameModel game) {
        return (piece.getColor().equals(StringConstants.WHITE.getCode()) && game.isWhite()) || (piece.getColor().equals(StringConstants.BLACK.getCode()) && !game.isWhite());
    }

    public static Tile findTileAt(List<List<Tile>> board, String sourceSquare) {
        Tile result = null;
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(sourceSquare)) {
                    result = tile;
                }
            }
        }
        return result;
    }


    public static List<List<Tile>> executeTheMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        if (GameService.kingIsCastling) {
            return handleCastlingMove(board, sourceSquare, targetSquare);

        } else {
            return handleStandardMove(board, sourceSquare, targetSquare);
        }
    }

    public static List<List<Tile>> handleCastlingMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        String rookToDisplace = GameService.localCastling.getCastlingActivity();
        Piece king = null;
        Piece rook = null;
        String kingLocation = "";
        String rookLocation = "";

        if (rookToDisplace.equals(StringConstants.ROOK_A1.getCode())) {
            king = new Piece("K", "w", true);
            rook = new Piece("R", "w", false);
            kingLocation = "c1";
            rookLocation = "d1";
        }
        if (rookToDisplace.equals(StringConstants.ROOK_H1.getCode())) {
            king = new Piece("K", "w", true);
            rook = new Piece("R", "w", false);
            kingLocation = "g1";
            rookLocation = "f1";
        }
        if (rookToDisplace.equals(StringConstants.ROOK_A8.getCode())) {
            king = new Piece("k", "b", true);
            rook = new Piece("r", "b", false);
            kingLocation = "c8";
            rookLocation = "d8";
        }
        if (rookToDisplace.equals(StringConstants.ROOK_H8.getCode())) {
            king = new Piece("k", "b", true);
            rook = new Piece("r", "b", false);
            kingLocation = "g8";
            rookLocation = "f8";
        }
        for (
                List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(kingLocation) && kingLocation != "") {
                    tile.setOccupied(true);
                    tile.setPiece(new Piece(king.getType(), king.getColor(), king.isKing()));
                }
                if (tile.getName().equals(rookLocation) && rookLocation != "") {
                    tile.setOccupied(true);
                    tile.setPiece(new Piece(rook.getType(), rook.getColor(), rook.isKing()));
                }
                if (tile.getName().equals(targetSquare)) {
                    tile.setOccupied(false);
                    tile.setPiece(new Piece("", "", false));
                }
                if (tile.getName().equals(sourceSquare)) {
                    tile.setOccupied(false);
                    tile.setPiece(new Piece("", "", false));
                }
            }
        }
        return board;
    }

    public static List<List<Tile>> handleStandardMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        Piece pieceToMove = null;
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(sourceSquare)) {
                    pieceToMove = tile.getPiece();
                    tile.setOccupied(false);
                    tile.setPiece(new Piece("", "", false));
                    break;
                }
                if (pieceToMove != null) {
                    break;
                }
            }
        }
        //Tile the piece lands at
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(targetSquare)) {
                    tile.setOccupied(true);
                    tile.setPiece(pieceToMove);
                    break;
                }
            }
        }
        //Special scenario: enPassant
        if (PawnService.isEnPassant) {
            for (List<Tile> row : board) {
                for (Tile tile : row) {
                    if (tile.getName().equals(PawnService.enPassant.fieldFigureToRemove())) {
                        tile.setOccupied(false);
                        tile.setPiece(new Piece("", "", false));
                        break;
                    }
                }
            }
        }
        return board;
    }

}

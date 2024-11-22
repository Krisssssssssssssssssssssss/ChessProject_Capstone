package com.example.backend.service;

import com.example.backend.constants.StringConstants;
import com.example.backend.dto.piece_movement.CastleResponse;
import com.example.backend.dto.piece_movement.EnPassant;
import com.example.backend.dto.piece_movement.KingAndThreats;
import com.example.backend.dto.piece_movement.MakeMoveRequest;
import com.example.backend.model.CastlingModel;
import com.example.backend.model.GameModel;
import com.example.backend.model.Piece;
import com.example.backend.model.Tile;
import com.example.backend.service.pieceMovement.*;
import com.example.backend.service.pieceMovement.helperMethods.Castling;
import com.example.backend.service.pieceMovement.helperMethods.PawnHelperMethods;

import java.util.ArrayList;
import java.util.List;

public class GameServiceHelper {

    //Sonar cloud suggestion
    private GameServiceHelper() {
        throw new UnsupportedOperationException("GameServiceHelpers is a utility class and cannot be instantiated.");
    }


    public static GameModel updateGameState(GameModel game, List<List<Tile>> gameBoard, MakeMoveRequest makeMoveRequest) {
        gameBoard = executeTheMove(gameBoard, makeMoveRequest.sourceSquare(), makeMoveRequest.targetSquare());
        if(!isKingSafe(gameBoard,game)) {
            return null;
        }
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
            case "r" -> canMove = RookService.canMove(board, sourceTile, targetTile);
            case "b" -> canMove = BishopService.canMove(board, sourceTile, targetTile);
            case "q" -> canMove = QueenService.canMove(board, sourceTile, targetTile, pieceToMove);
            case "k" -> canMove = KingService.canMove(sourceTile, targetTile);
            default -> canMove = false;
        }
        return canMove;
    }

    public static boolean isKingSafe(List<List<Tile>> board, GameModel game) {
        KingAndThreats kingAndThreats = new KingAndThreats(null, new ArrayList<>());

        //It is reversed logic and !isWhite when we want to check if it is white, because we are passing
        //  the updated board with the move already made, so the move is automatically flipped to the black
        if (game.isWhite()) { //should be seen as isWhite
            findKingPositionAndPotentialThreat(board, StringConstants.BLACK.getCode(), StringConstants.WHITE.getCode(), kingAndThreats);
            return checkKingSafe(board, kingAndThreats, game);
        } else {
            findKingPositionAndPotentialThreat(board, StringConstants.WHITE.getCode(), StringConstants.BLACK.getCode(), kingAndThreats);
            return checkKingSafe(board, kingAndThreats, game);
        }
    }

    private static void findKingPositionAndPotentialThreat(List<List<Tile>> board, String threateningColour, String kingsColour, KingAndThreats kingAndThreats) {
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.isOccupied() && tile.getPiece().getColor().equals(threateningColour)) {
                    kingAndThreats.getOpposingPieces().add(tile);
                }
                if (tile.getPiece().isKing() && tile.getPiece().getColor().equals(kingsColour)) {
                    kingAndThreats.setKingsTile(tile);
                }
            }
        }
    }

    private static boolean checkKingSafe(List<List<Tile>> board, KingAndThreats kingAndThreats, GameModel game) {
        boolean canMove;
        for (Tile opposingTile : kingAndThreats.getOpposingPieces()) {
            switch (opposingTile.getPiece().getType().toLowerCase()) {
                case "p" -> canMove = PawnHelperMethods.simulatedCanMove(opposingTile, kingAndThreats.getKingsTile(), opposingTile.getPiece(), game);
                case "n" -> canMove = KnightService.canMove(opposingTile, kingAndThreats.getKingsTile(), opposingTile.getPiece());
                case "r" -> canMove = RookService.canMove(board, opposingTile, kingAndThreats.getKingsTile());
                case "b" -> canMove = BishopService.canMove(board, opposingTile, kingAndThreats.getKingsTile());
                case "q" -> canMove = QueenService.canMove(board, opposingTile, kingAndThreats.getKingsTile(), opposingTile.getPiece());
                case "k" -> canMove = KingService.canMove(opposingTile, kingAndThreats.getKingsTile());
                default ->  canMove = false;
            }
            if (canMove) {
                return false;
            }
        }
        return true;
    }

    public static boolean isThatKingAlreadyCastled(Piece pieceToMove, GameModel game) {
        String pieceColor = pieceToMove.getColor();
        CastlingModel castlingModel = game.getCastlingModel();

        return (pieceColor.equals(StringConstants.WHITE.getCode()) && castlingModel.isWhiteKingMoved()) ||
                (pieceColor.equals(StringConstants.BLACK.getCode()) && castlingModel.isBlackKingMoved());
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
                if (tile.getName().equals(kingLocation) && !kingLocation.equals("")) {
                    tile.setOccupied(true);
                    tile.setPiece(new Piece(king.getType(), king.getColor(), king.isKing()));
                }
                if (tile.getName().equals(rookLocation) && !rookLocation.equals("")) {
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
        //Remove the piece from the tile it moves away from
        Piece pieceToMove = removePieceFromTile(board, sourceSquare);

        //Tile the piece lands at
        if (pieceToMove != null) {
            placePieceOnTile(board, targetSquare, pieceToMove);
        }
        //Special scenario: enPassant
        if (PawnService.isEnPassant) {
            removePieceFromTile(board, PawnService.enPassant.fieldFigureToRemove());
        }

        return board;
    }

    private static Piece removePieceFromTile(List<List<Tile>> board, String tileName) {
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(tileName)) {
                    Piece pieceToMove = tile.getPiece();
                    tile.setOccupied(false);
                    tile.setPiece(new Piece("", "", false));
                    return pieceToMove;
                }
            }
        }
        return null;
    }

    private static void placePieceOnTile(List<List<Tile>> board, String tileName, Piece pieceToMove) {
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(tileName)) {
                    tile.setOccupied(true);
                    tile.setPiece(pieceToMove);
                    return;
                }
            }
        }
    }

}

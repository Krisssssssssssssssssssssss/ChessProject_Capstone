package com.example.backend.service;

import com.example.backend.constants.StringConstants;
import com.example.backend.dto.pieceMovement.CastleResponse;
import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.dto.pieceMovement.MakeMoveRequest;
import com.example.backend.exception.GameNotFoundException;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.model.CastlingModel;
import com.example.backend.model.GameModel;
import com.example.backend.model.Pieces.*;
import com.example.backend.model.Tile;
import com.example.backend.repository.GameRepository;
import com.example.backend.service.pieceMovement.PawnService;
import com.example.backend.service.pieceMovement.helperMethods.Castling;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final IdService idService;
    private static boolean kingIsCastling = false;
    public static CastlingModel localCastling;

    public GameModel createGame(GameModel gameModel) throws Exception {
        Optional<GameModel> existingGame = gameRepository.findGameByPlayerOneIdAndPlayerTwoId(gameModel.getPlayerOneId(), gameModel.getPlayerTwoId());

        if (existingGame.isPresent()) {
            throw new UserAlreadyExistsException("Game already exists");
        }
        gameModel.setId(idService.getRandomId());
        return gameRepository.save(gameModel);
    }

    public GameModel getGame(String playerId, String playerTwoId) {
        return gameRepository.findGameByPlayerOneIdAndPlayerTwoId(playerId, playerTwoId)
                .orElseThrow(() -> new GameNotFoundException("Game not found for the selected players players"));
    }

    public boolean doesGameExist(String playerId, String playerTwoId) {
        Optional<GameModel> existingGame = gameRepository.findGameByPlayerOneIdAndPlayerTwoId(playerId, playerTwoId);
        return existingGame.isPresent();
    }

    public String makeMove(MakeMoveRequest makeMoveRequest) {
        GameModel game = fetchGame(makeMoveRequest.playerOneId(), makeMoveRequest.playerTwoId());
        localCastling = game.getCastlingModel();
        List<List<Tile>> gameBoard = FenConverter.toBoardArray(game.getFenString());

        if (!isMoveAllowed(gameBoard, makeMoveRequest.sourceSquare(), makeMoveRequest.targetSquare(), game)) {
            return game.getFenString();
        }

        String updatedFen = updateGameState(game, gameBoard, makeMoveRequest);
        gameRepository.save(game);
        resetTemporaryStates();
        return updatedFen;
    }

    private GameModel fetchGame(String playerOneId, String playerTwoId) {
        return this.getGame(playerOneId, playerTwoId);
    }

    private String updateGameState(GameModel game, List<List<Tile>> gameBoard, MakeMoveRequest makeMoveRequest) {
        gameBoard = executeTheMove(gameBoard, makeMoveRequest.sourceSquare(), makeMoveRequest.targetSquare());
        String fenString = FenConverter.toFen(gameBoard);
        game.setFenString(fenString);
        game.setWhite(!game.isWhite());
        game.setEnPassant(PawnService.enPassant);
        game.setCastlingModel(localCastling);
        return fenString;
    }

    private void resetTemporaryStates() {
        PawnService.enPassant = new EnPassant("", "");
        kingIsCastling = false;
    }


    public boolean isMoveAllowed(List<List<Tile>> board, String sourceSquare, String targetSquare, GameModel game) {
        boolean canMove = false;
        Piece pieceToMove = null;
        Tile sourceTile = null;
        //Turn based movement
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(sourceSquare)) {
                    pieceToMove = tile.getPiece();
                    sourceTile = tile;
                    if ((pieceToMove.getColor().equals("w") && !game.isWhite()) ||
                            (pieceToMove.getColor().equals("b") && game.isWhite())) {
                        return false;
                    }
                }
            }
        }
        //If it's their turn, is the move legal?
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(targetSquare)) {
                    Piece pieceToTake = tile.getPiece();
                    //Don't allow to take same colour unless it's a castle
                    if ((sourceTile.getPiece().getColor().equals(pieceToTake.getColor()))) {
                        if (sourceTile.getPiece().isKing()
                                && tile.getPiece().getType().equalsIgnoreCase("r")
                        ) {
                            CastleResponse castleResponse = Castling.canKingCastle(board, sourceTile, tile, game);
                            if (castleResponse.isKingCanCastle()) {
                                kingIsCastling = true;
                                localCastling = castleResponse.getCastlingModel();
                                return true;
                            }
                        } else {
                            return false;
                        }
                    }
                    if (!pieceToMove.getType().toLowerCase().equals("p")) {
                        PawnService.enPassant = new EnPassant("", "");
                    }
                    switch (pieceToMove.getType().toLowerCase()) {
                        case "p" -> canMove = Pawn.canMove(board, sourceSquare, targetSquare, game);
                        case "n" -> canMove = Knight.canMove(board, sourceSquare, targetSquare);
                        case "r" -> canMove = Rook.canMove(board, sourceSquare, targetSquare);
                        case "q" -> canMove = Queen.canMove(board, sourceSquare, targetSquare);
                        case "k" -> canMove = King.canMove(board, sourceSquare, targetSquare);
                        case "b" -> canMove = Bishop.canMove(board, sourceSquare, targetSquare);
                        default -> throw new IllegalArgumentException("Unknown piece type: " + pieceToMove.getType());
                    }
                }
            }

        }
        return canMove;
    }

    private List<List<Tile>> executeTheMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {

        if (kingIsCastling) {
            return handleCastlingMove(board, sourceSquare, targetSquare);

        } else {
            return handleStandardMove(board, sourceSquare, targetSquare);
        }
    }

    private List<List<Tile>> handleCastlingMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        String rookToDisplace = localCastling.getCastlingActivity();
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

    private List<List<Tile>> handleStandardMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
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

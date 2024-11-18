package com.example.backend.service;

import com.example.backend.dto.pieceMovement.EnPassant;
import com.example.backend.dto.pieceMovement.MakeMoveRequest;
import com.example.backend.exception.GameNotFoundException;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.model.GameModel;
import com.example.backend.model.Pieces.*;
import com.example.backend.model.Tile;
import com.example.backend.repository.GameRepository;
import com.example.backend.service.pieceMovement.PawnService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final IdService idService;

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
        GameModel game = this.getGame(makeMoveRequest.playerOneId(), makeMoveRequest.playerTwoId());
        List<List<Tile>> gameBoard = FenConverter.toBoardArray(game.getFenString());
        boolean isMoveAllowed = isMoveAllowed(gameBoard, makeMoveRequest.sourceSquare(), makeMoveRequest.targetSquare(), game);
        if (isMoveAllowed) {
            gameBoard = this.executeTheMove(gameBoard, makeMoveRequest.sourceSquare(), makeMoveRequest.targetSquare());
            String editedFen = FenConverter.toFen(gameBoard);
            game.setFenString(editedFen);
            game.setWhite(!game.isWhite());
            game.setEnPassant(PawnService.enPassant);
            gameRepository.save(game);
            PawnService.enPassant = new EnPassant("", "");
            return editedFen;
        } else {
            return game.getFenString();
        }
    }

    public boolean isMoveAllowed(List<List<Tile>> board, String sourceSquare, String targetSquare, GameModel game) {
        boolean canMove = false;
        Piece pieceToMove = null;
        //Turn based movement
        for (List<Tile> row : board) {
            for (Tile tile : row) {
                if (tile.getName().equals(sourceSquare)) {
                    pieceToMove = tile.getPiece();
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
                    if ((pieceToMove.getColor().equals(pieceToTake.getColor()))) {
                        return false;
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
                    if (!pieceToMove.getType().toLowerCase().equals("p")) {
                        PawnService.enPassant = new EnPassant("", "");
                    }
                }
            }

        }
        return canMove;
    }

    private List<List<Tile>> executeTheMove(List<List<Tile>> board, String sourceSquare, String targetSquare) {
        Piece pieceToMove = null;
        //Tile the piece moves from
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

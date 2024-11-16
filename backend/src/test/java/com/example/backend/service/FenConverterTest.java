package com.example.backend.service;

import com.example.backend.model.Piece;
import com.example.backend.model.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FenConverterTest {

    @Test
    void toFen_emptyBoard() {
        List<List<Tile>> emptyBoard = createEmptyBoard();

        String result = FenConverter.toFen(emptyBoard);

        assertEquals("8/8/8/8/8/8/8/8", result);
    }

    @Test
    void toFen_withPieces() {
        List<List<Tile>> board = createEmptyBoard();
        board.get(7).get(0).setPiece(new Piece("R", "w", false)); // White Rook on rank 1
        board.get(7).get(1).setPiece(new Piece("N", "w", false)); // White Knight on rank 1
        board.get(6).get(0).setPiece(new Piece("p", "b", false)); // Black Pawn on rank 2

        String result = FenConverter.toFen(board);

        assertEquals("8/8/8/8/8/8/p7/RN6", result);
    }


    @Test
    void toFen_withMixedPiecesAndEmptySquares() {
        List<List<Tile>> board = createEmptyBoard();
        board.get(0).get(0).setPiece(new Piece("Q", "w", false)); // White Queen
        board.get(0).get(7).setPiece(new Piece("k", "b", true));  // Black King
        board.get(7).get(3).setPiece(new Piece("P", "w", false)); // White Pawn

        String result = FenConverter.toFen(board);

        assertEquals("Q6k/8/8/8/8/8/8/3P4", result);
    }

    @Test
    void toBoardArray_emptyBoardFen() {
        String fenString = "8/8/8/8/8/8/8/8";

        List<List<Tile>> result = FenConverter.toBoardArray(fenString);

        for (List<Tile> row : result) {
            for (Tile tile : row) {
                assertFalse(tile.isOccupied());
                assertEquals("", tile.getPiece().getType());
            }
        }
    }

    @Test
    void toBoardArray_withPiecesFen() {
        String fenString = "R3k3/8/8/8/8/8/8/4P3";

        List<List<Tile>> result = FenConverter.toBoardArray(fenString);

        // Validate specific pieces
        Tile rookTile = result.get(0).get(0);
        assertTrue(rookTile.isOccupied());
        assertEquals("R", rookTile.getPiece().getType());
        assertEquals("w", rookTile.getPiece().getColor());

        Tile kingTile = result.get(0).get(4);
        assertTrue(kingTile.isOccupied());
        assertEquals("k", kingTile.getPiece().getType());
        assertEquals("b", kingTile.getPiece().getColor());

        Tile pawnTile = result.get(7).get(4);
        assertTrue(pawnTile.isOccupied());
        assertEquals("P", pawnTile.getPiece().getType());
        assertEquals("w", pawnTile.getPiece().getColor());
    }

    @Test
    void toBoardArray_withMixedPieces() {
        String fenString = "RNBQKBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbqkbnr";

        List<List<Tile>> result = FenConverter.toBoardArray(fenString);

        // Validate top row
        assertEquals("R", result.get(0).get(0).getPiece().getType());
        assertEquals("N", result.get(0).get(1).getPiece().getType());
        assertEquals("Q", result.get(0).get(3).getPiece().getType());

        // Validate bottom row
        assertEquals("r", result.get(7).get(0).getPiece().getType());
        assertEquals("n", result.get(7).get(1).getPiece().getType());
        assertEquals("q", result.get(7).get(3).getPiece().getType());
    }

    private List<List<Tile>> createEmptyBoard() {
        List<List<Tile>> board = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            List<Tile> row = new ArrayList<>();
            for (int x = 0; x < 8; x++) {
                row.add(new Tile(
                        "" + (char) ('a' + x) + (8 - y),
                        y * 8 + x,
                        x,
                        y,
                        false,
                        new Piece("", "", false)
                ));
            }
            board.add(row);
        }
        return board;
    }
}

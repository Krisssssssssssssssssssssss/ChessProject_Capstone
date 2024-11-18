package com.example.backend.service;

import com.example.backend.model.Tile;
import com.example.backend.model.Pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class FenConverter {

    private static final int BOARD_SIZE = 8;

    public static String toFen(List<List<Tile>> boardArray) {
        StringBuilder fenBuilder = new StringBuilder();

        for (int y = 0; y < BOARD_SIZE; y++) {
            int emptyCount = 0;

            for (int x = 0; x < BOARD_SIZE; x++) {
                // Get the Tile at (x, y)
                Tile tile = boardArray.get(y).get(x);
                // Get the Piece on the Tile
                Piece piece = tile.getPiece();

                if (piece != null && !piece.getType().isEmpty()) {
                    if (emptyCount > 0) {
                        fenBuilder.append(emptyCount);
                        emptyCount = 0;
                    }
                    fenBuilder.append(piece.getType());
                } else {
                    emptyCount++;
                }
            }

            if (emptyCount > 0) {
                fenBuilder.append(emptyCount);
            }
            if (y < BOARD_SIZE - 1) {
                fenBuilder.append('/');
            }
        }
        return fenBuilder.toString();
    }

    public static List<List<Tile>> toBoardArray(String fenString) {
        List<List<Tile>> boardArray = new ArrayList<>();
        String[] rows = fenString.split("/");

        for (int y = 0; y < BOARD_SIZE; y++) {
            List<Tile> rowList = new ArrayList<>();
            int x = 0;

            for (char ch : rows[y].toCharArray()) {
                if (Character.isDigit(ch)) {
                    int emptySquares = Character.getNumericValue(ch);

                    for (int i = 0; i < emptySquares; i++) {
                        rowList.add(new Tile(
                                getTileName(x, y),
                                y * BOARD_SIZE + x,
                                x,
                                y,
                                false,
                                new Piece("", "", false)
                        ));
                        x++;
                    }
                } else {
                    Piece piece = createPieceFromFenChar(ch);
                    rowList.add(new Tile(
                            getTileName(x, y),  // Name using x and y
                            y * BOARD_SIZE + x, // ID based on x and y
                            x,                  // x-coordinate
                            y,                  // y-coordinate
                            true,               // Occupied
                            piece
                    ));
                    x++;
                }
            }

            boardArray.add(rowList);
        }

        return boardArray;
    }


    private static Piece createPieceFromFenChar(char ch) {
        String type = String.valueOf(ch);
        String color = Character.isUpperCase(ch) ? "w" : "b";
        boolean isKing = type.equalsIgnoreCase("k");

        return new Piece(type, color, isKing);
    }

    private static String getTileName(int x, int y) {
        char file = (char) ('a' + x);
        //-y because it starts from top with the id=0
        int rank = BOARD_SIZE - y;
        return "" + file + rank;
    }
}

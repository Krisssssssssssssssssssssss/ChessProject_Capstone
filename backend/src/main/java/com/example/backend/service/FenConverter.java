package com.example.backend.service;

import com.example.backend.model.Tile;

import java.util.ArrayList;
import java.util.List;

public class FenConverter {

    private static final int BOARD_SIZE = 8;

    public static String toFen(List<Tile> tiles) {
        StringBuilder fenBuilder = new StringBuilder();
        for (int row = 0; row < BOARD_SIZE; row++) {
            int emptyCount = 0;
            for (int col = 0; col < BOARD_SIZE; col++) {
                Tile tile = tiles.get(row * BOARD_SIZE + col);
                if (tile.isOccupied()) {
                    if (emptyCount > 0) {
                        fenBuilder.append(emptyCount);
                        emptyCount = 0;
                    }
                    fenBuilder.append(tile.getPiece());
                } else {
                    emptyCount++;
                }
            }
            if (emptyCount > 0) {
                fenBuilder.append(emptyCount);
            }
            if (row < BOARD_SIZE - 1) {
                fenBuilder.append('/');
            }
        }
        return fenBuilder.toString();
    }

    public static List<Tile> toList(String fenString) {
        List<Tile> tiles = new ArrayList<>();
        String[] rows = fenString.split("/");
        for (int row = 0; row < BOARD_SIZE; row++) {
            int col = 0;
            for (char ch : rows[row].toCharArray()) {
                if (Character.isDigit(ch)) {
                    int emptySquares = Character.getNumericValue(ch);
                    for (int i = 0; i < emptySquares; i++) {
                        tiles.add(new Tile("Tile", row * BOARD_SIZE + col, col, row, false, (row + col) % 2 == 0 ? "white" : "black", ""));
                        col++;
                    }
                } else {
                    String piece = String.valueOf(ch);
                    boolean isWhite = Character.isUpperCase(ch);
                    tiles.add(new Tile("Tile", row * BOARD_SIZE + col, col, row, true, (row + col) % 2 == 0 ? "white" : "black", piece));
                    col++;
                }
            }
        }
        return tiles;
    }
}

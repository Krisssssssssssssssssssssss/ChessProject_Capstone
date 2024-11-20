package com.example.backend.constants;

import lombok.Getter;

public enum StringConstants {
    WHITE("w"),
    BLACK("b"),
    PAWN("p"),
    ROOK("r"),
    KNIGHT("n"),
    BISHOP("b"),
    QUEEN("q"),
    KING("k"),
    ROOK_A1("a1"),
    ROOK_H1("h1"),
    ROOK_A8("a8"),
    ROOK_H8("h8");

    @Getter
    private final String code;

    StringConstants(String code) {
        this.code = code;
    }
}

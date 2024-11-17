package com.example.backend.model.Pieces;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class Piece {
    String type;
    String color;
    boolean isKing;
}
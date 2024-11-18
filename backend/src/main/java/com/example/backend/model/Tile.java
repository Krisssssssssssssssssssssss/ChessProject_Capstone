package com.example.backend.model;

import lombok.*;
import com.example.backend.model.Pieces.Piece;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class Tile {
    String name;
    int id;
    int x;
    int y;
    boolean isOccupied;
    Piece piece;
}
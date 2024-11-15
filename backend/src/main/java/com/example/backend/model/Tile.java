package com.example.backend.model;

import lombok.*;

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
    String color;
    String piece;
}

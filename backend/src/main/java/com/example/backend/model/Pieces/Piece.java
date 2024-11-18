package com.example.backend.model.Pieces;

import com.example.backend.model.Tile;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Piece {
    private String type;
    private String color;
    private boolean isKing;
}

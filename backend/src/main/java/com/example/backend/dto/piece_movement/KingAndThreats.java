package com.example.backend.dto.piece_movement;

import com.example.backend.model.Tile;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class KingAndThreats {
    public Tile kingsTile;
    public List<Tile> opposingPieces;
}

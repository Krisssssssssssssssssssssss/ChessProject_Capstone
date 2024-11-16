package com.example.backend.model;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class Board {
    public List<List<Tile>> boardArray;
    public String fen;
}
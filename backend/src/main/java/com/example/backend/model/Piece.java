package com.example.backend.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Piece {
    private String type;
    private String color;
    private boolean isKing;
}

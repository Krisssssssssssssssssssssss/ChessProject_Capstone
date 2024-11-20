package com.example.backend.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class CastlingModel {
    boolean isWhiteKingMoved;
    boolean isBlackKingMoved;
    boolean isRookA1Moved;
    boolean isRookA8Moved;
    boolean isRookH1Moved;
    boolean isRookH8Moved;
    String castlingActivity;
}

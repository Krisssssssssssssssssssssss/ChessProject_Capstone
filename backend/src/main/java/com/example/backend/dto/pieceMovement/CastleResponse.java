package com.example.backend.dto.pieceMovement;

import com.example.backend.model.CastlingModel;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class CastleResponse {
    boolean kingCanCastle;
    CastlingModel castlingModel;
}

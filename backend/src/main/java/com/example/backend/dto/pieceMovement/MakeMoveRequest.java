package com.example.backend.dto.pieceMovement;

public record MakeMoveRequest (String playerOneId, String playerTwoId, String sourceSquare, String targetSquare){
}

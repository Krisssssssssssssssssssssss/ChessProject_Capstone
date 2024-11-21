package com.example.backend.dto.piece_movement;

public record MakeMoveRequest (String playerOneId, String playerTwoId, String sourceSquare, String targetSquare){
}

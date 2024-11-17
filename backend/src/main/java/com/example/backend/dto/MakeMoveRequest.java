package com.example.backend.dto;

public record MakeMoveRequest (String playerOneId, String playerTwoId, String sourceSquare, String targetSquare){
}

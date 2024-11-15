package com.example.backend.dto;

public record MakeMoveRequest (String playerOneId, String playerTwoId, String sourceField, String targetField){
}

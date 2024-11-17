package com.example.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
@Document("Game")
public class GameModel {
    @Id
    private String id;
    private String fenString;
    private String playerOneId;
    private String playerTwoId;
    private boolean isWhite;
}

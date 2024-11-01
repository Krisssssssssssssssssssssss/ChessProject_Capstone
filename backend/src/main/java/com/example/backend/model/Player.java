package com.example.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Builder
@With
@Document("Player")
public record Player (String id, String name){
}

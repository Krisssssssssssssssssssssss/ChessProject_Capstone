package com.example.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
@Document("Player")
public class UserModel {
    @Id
    private String id;
    private String name;
    private String password;
    private String rating;
    private Boolean isAdmin;
    private Boolean isGitHubUser;
}

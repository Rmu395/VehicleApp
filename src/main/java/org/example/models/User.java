package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String id;
    private String login;
    private String password;
    private String role;


    // login;haslo,rola:
    // lukasz;lukasz123,ADMIN
    // kamil,kamil123,USER
}

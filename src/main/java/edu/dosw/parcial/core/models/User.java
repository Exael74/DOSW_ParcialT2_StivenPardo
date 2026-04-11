package edu.dosw.parcial.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String userId;
    private String name;
    private String email;
    private String password;      
    private String passwordHash;  
    private UserRole role;
    private Vehicle vehicle;      
}
package edu.dosw.parcial.controller.dtos.response;
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

public class RegisterResponseDTO {
    private String userId;
    private String name;
    private String email;
    private String role;
    private String message;
}
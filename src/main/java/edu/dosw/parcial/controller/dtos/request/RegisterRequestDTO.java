package edu.dosw.parcial.controller.dtos.request;
import edu.dosw.parcial.core.models.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener formato valido")
    private String email;
    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 8, message = "La contrasena debe tener minimo 8 caracteres")
    private String password;
    @NotNull(message = "El rol es obligatorio")
    private UserRole role;
    private String licensePlate;
    private String brand;
    private String model;
}
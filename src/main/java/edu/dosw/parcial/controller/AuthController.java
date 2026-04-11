package edu.dosw.parcial.controller;
import edu.dosw.parcial.controller.dtos.request.RegisterRequestDTO;
import edu.dosw.parcial.controller.dtos.response.RegisterResponseDTO;
import edu.dosw.parcial.controller.mappers.UserControllerMapper;
import edu.dosw.parcial.core.models.User;
import edu.dosw.parcial.core.models.UserRole;
import edu.dosw.parcial.core.models.Vehicle;
import edu.dosw.parcial.core.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserControllerMapper userControllerMapper;
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request) {
        // 1. DTO -> Modelo de dominio
        User user = userControllerMapper.toDomain(request);
        // 2. Si el rol es DRIVER, construir el vehiculo desde el DTO
        if (UserRole.DRIVER.equals(request.getRole())
                && request.getLicensePlate() != null
                && !request.getLicensePlate().isBlank()) {
            user.setVehicle(Vehicle.builder()
                    .licensePlate(request.getLicensePlate())
                    .brand(request.getBrand())
                    .model(request.getModel())
                    .build());
        }
        // 3. Ejecutar el caso de uso
        User registered = userService.register(user);
        // 4. Construir respuesta
        RegisterResponseDTO response = RegisterResponseDTO.builder()
                .userId(registered.getUserId())
                .name(registered.getName())
                .email(registered.getEmail())
                .role(registered.getRole().name())
                .message("Usuario registrado exitosamente")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
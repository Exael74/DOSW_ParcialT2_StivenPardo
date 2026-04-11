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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para registro y autenticación de usuarios")
public class AuthController {
    private final UserService userService;
    private final UserControllerMapper userControllerMapper;
    
    @Operation(summary = "Registrar un usuario nuevo", description = "Registra a un pasajero o conductor validando las reglas de negocio, incluyendo su información vehicular si aplica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en la estructura o reglas de negocio", 
                content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto: El correo ya se encuentra registrado", 
                content = @Content)
    })
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
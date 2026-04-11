package edu.dosw.parcial.controller.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripRequestDTO {

    @NotBlank(message = "El ID del pasajero es obligatorio")
    private String passengerId;

    @NotNull(message = "El origen no puede ser nulo")
    @NotBlank(message = "El origen es obligatorio")
    private String origin;

    @NotNull(message = "El destino no debe estar nulo")
    @NotBlank(message = "El destino es obligatorio")
    private String destination;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double price;
    
    private String additionalNotes;
}

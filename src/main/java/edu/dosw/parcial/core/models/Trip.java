package edu.eci.dosw.parcial.core.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class trip{
    @NotBlank(message = "Trip ID cannot be blank")
    private String tripId;

    @NotBlank(message = "Passenger cannot be blank")
    private User passenger;

    @NotBlank(message = "Vehicle cannot be blank")
    private Vehicle vehicle;

    @NotBlank(message = "Start date cannot be blank")
    private LocalDate startDate;

    @NotBlank(message = "End date cannot be blank")
    private LocalDate endDate;

    @NotBlank(message = "Origin cannot be blank")
    private LocalDate origin; 

    @NotBlank(message = "Destination cannot be blank")
    private LocalDate destination; 

    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotBlank(message = "Driver cannot be blank")
    private User driver;
}
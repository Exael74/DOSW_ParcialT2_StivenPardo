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


public class Vehicle {
    private String vehicleId;
    private String licensePlate;
    private String brand;
    private String model;
}
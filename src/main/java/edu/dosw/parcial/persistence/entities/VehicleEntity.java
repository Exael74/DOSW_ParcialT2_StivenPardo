package edu.dosw.parcial.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String vehicleId;

    @Column(unique = true, nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}

package edu.dosw.parcial.persistence.entities;

import edu.dosw.parcial.core.models.TripStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tripId;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status;

    @Column(name = "is_active_flag", nullable = false)
    private Boolean isTripActive;

    @Column(name = "additional_notes")
    private String additionalNotes;

    // Relación ManyToOne con el pasajero
    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private UserEntity passenger;

    // Relación ManyToOne con el conductor (puede ser nulo inicialmente)
    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = true)
    private UserEntity driver;
}

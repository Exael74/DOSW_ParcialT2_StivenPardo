package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {
    
    Optional<VehicleEntity> findByLicensePlate(String licensePlate);
    
    boolean existsByLicensePlate(String licensePlate);
}

package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.core.models.TripStatus;
import edu.dosw.parcial.persistence.entities.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<TripEntity, String> {
    
    List<TripEntity> findTripEntitiesByStatusIs(TripStatus status);
    
    boolean existsTripEntityByPassengerAndStatusIs(UserEntity passenger, TripStatus status);
}

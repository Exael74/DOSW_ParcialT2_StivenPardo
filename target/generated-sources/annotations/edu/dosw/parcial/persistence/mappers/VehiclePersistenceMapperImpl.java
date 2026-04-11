package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Vehicle;
import edu.dosw.parcial.persistence.entities.VehicleEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-11T18:13:50-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class VehiclePersistenceMapperImpl implements VehiclePersistenceMapper {

    @Override
    public VehicleEntity toEntity(Vehicle vehicle) {
        if ( vehicle == null ) {
            return null;
        }

        VehicleEntity.VehicleEntityBuilder vehicleEntity = VehicleEntity.builder();

        vehicleEntity.brand( vehicle.getBrand() );
        vehicleEntity.licensePlate( vehicle.getLicensePlate() );
        vehicleEntity.model( vehicle.getModel() );
        vehicleEntity.vehicleId( vehicle.getVehicleId() );

        return vehicleEntity.build();
    }

    @Override
    public Vehicle toDomain(VehicleEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Vehicle.VehicleBuilder vehicle = Vehicle.builder();

        vehicle.vehicleId( entity.getVehicleId() );
        vehicle.brand( entity.getBrand() );
        vehicle.licensePlate( entity.getLicensePlate() );
        vehicle.model( entity.getModel() );

        return vehicle.build();
    }
}

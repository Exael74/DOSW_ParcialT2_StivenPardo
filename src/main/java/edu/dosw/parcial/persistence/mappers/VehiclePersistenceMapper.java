package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Vehicle;
import edu.dosw.parcial.persistence.entities.VehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehiclePersistenceMapper {

    // user is set manually in the service after mapping
    @Mapping(target = "user", ignore = true)
    VehicleEntity toEntity(Vehicle vehicle);

    @Mapping(target = "vehicleId", source = "vehicleId")
    Vehicle toDomain(VehicleEntity entity);
}

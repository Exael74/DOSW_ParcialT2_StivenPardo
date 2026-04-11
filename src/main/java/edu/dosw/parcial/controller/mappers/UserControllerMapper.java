package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.request.RegisterRequestDTO;
import edu.dosw.parcial.core.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserControllerMapper {
    // vehicleId, passwordHash y vehicle se manejan fuera del mapper
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    User toDomain(RegisterRequestDTO dto);
}
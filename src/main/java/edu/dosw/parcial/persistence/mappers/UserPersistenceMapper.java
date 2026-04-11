package edu.dosw.parcial.persistence.mappers;
import edu.dosw.parcial.core.models.User;
import edu.dosw.parcial.persistence.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")


public interface UserPersistenceMapper {
    // El vehiculo se asigna manualmente en el servicio 
    @Mapping(target = "vehicle", ignore = true)
    UserEntity toEntity(User user);
    
    // password en texto plano no existe en la entidad, se ignora
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    User toDomain(UserEntity entity);
}
package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.request.RegisterRequestDTO;
import edu.dosw.parcial.core.models.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-11T18:17:33-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserControllerMapperImpl implements UserControllerMapper {

    @Override
    public User toDomain(RegisterRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( dto.getName() );
        user.email( dto.getEmail() );
        user.password( dto.getPassword() );
        user.role( dto.getRole() );

        return user.build();
    }
}

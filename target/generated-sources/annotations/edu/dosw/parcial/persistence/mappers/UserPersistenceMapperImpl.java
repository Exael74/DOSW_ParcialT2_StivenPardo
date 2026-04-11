package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.User;
import edu.dosw.parcial.persistence.entities.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-11T18:17:33-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserPersistenceMapperImpl implements UserPersistenceMapper {

    @Override
    public UserEntity toEntity(User user) {
        if ( user == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.userId( user.getUserId() );
        userEntity.name( user.getName() );
        userEntity.email( user.getEmail() );
        userEntity.passwordHash( user.getPasswordHash() );
        userEntity.role( user.getRole() );

        return userEntity.build();
    }

    @Override
    public User toDomain(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.userId( entity.getUserId() );
        user.name( entity.getName() );
        user.email( entity.getEmail() );
        user.passwordHash( entity.getPasswordHash() );
        user.role( entity.getRole() );

        return user.build();
    }
}

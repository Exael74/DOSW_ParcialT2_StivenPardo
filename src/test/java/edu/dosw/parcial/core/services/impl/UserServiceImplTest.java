package edu.dosw.parcial.core.services.impl;

import edu.dosw.parcial.core.models.User;
import edu.dosw.parcial.core.models.UserRole;
import edu.dosw.parcial.core.validators.UserValidator;
import edu.dosw.parcial.persistence.entities.UserEntity;
import edu.dosw.parcial.persistence.mappers.UserPersistenceMapper;
import edu.dosw.parcial.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_ReturnsRegisteredUser_WhenDataIsValid() {
        // Arrange
        User inputUser = User.builder()
                .email("test@mail.escuelaing.edu.co")
                .password("plain123")
                .role(UserRole.PASSENGER)
                .build();

        UserEntity savedEntity = new UserEntity();
        User domainResult = new User();

        doNothing().when(userValidator).validateForRegistration(inputUser);
        when(passwordEncoder.encode("plain123")).thenReturn("hashedPass");
        when(userPersistenceMapper.toEntity(inputUser)).thenReturn(new UserEntity());
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
        when(userPersistenceMapper.toDomain(savedEntity)).thenReturn(domainResult);

        // Act
        User result = userService.register(inputUser);

        // Assert
        assertNotNull(result);
        verify(userValidator).validateForRegistration(inputUser);
        verify(passwordEncoder).encode("plain123");
        verify(userRepository).save(any(UserEntity.class));
    }
}

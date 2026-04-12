package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.models.User;
import edu.dosw.parcial.core.models.UserRole;
import edu.dosw.parcial.core.models.Vehicle;
import edu.dosw.parcial.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    private User validPassenger;
    private User validDriver;

    @BeforeEach
    void setUp() {
        validPassenger = User.builder()
                .email("pasajero@mail.escuelaing.edu.co")
                .role(UserRole.PASSENGER)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .licensePlate("ABC-123")
                .brand("Toyota")
                .model("Corolla")
                .build();

        validDriver = User.builder()
                .email("conductor@mail.escuelaing.edu.co")
                .role(UserRole.DRIVER)
                .vehicle(vehicle)
                .build();
    }

    @Test
    void validateForRegistration_ReturnsOk_WhenPassengerIsValid() {
        when(userRepository.existsByEmail(validPassenger.getEmail())).thenReturn(false);
        assertDoesNotThrow(() -> userValidator.validateForRegistration(validPassenger));
    }

    @Test
    void validateForRegistration_ThrowsException_WhenEmailHasWrongDomain() {
        validPassenger.setEmail("invalido@gmail.com");
        assertThrows(IllegalArgumentException.class, () -> userValidator.validateForRegistration(validPassenger));
    }

    @Test
    void validateForRegistration_ThrowsException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(validPassenger.getEmail())).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> userValidator.validateForRegistration(validPassenger));
    }

    @Test
    void validateForRegistration_ThrowsException_WhenDriverHasNoVehicle() {
        validDriver.setVehicle(null);
        when(userRepository.existsByEmail(validDriver.getEmail())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userValidator.validateForRegistration(validDriver));
    }
}

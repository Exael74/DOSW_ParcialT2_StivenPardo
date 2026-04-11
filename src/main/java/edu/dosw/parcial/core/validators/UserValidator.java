package edu.dosw.parcial.core.validators;
import edu.dosw.parcial.core.models.User;
import edu.dosw.parcial.core.models.UserRole;
import edu.dosw.parcial.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class UserValidator {
    private static final String INSTITUTIONAL_DOMAIN = "@mail.escuelaing.edu.co";
    private final UserRepository userRepository;
    public void validateForRegistration(User user) {
        validateInstitutionalEmail(user.getEmail());
        validateEmailNotTaken(user.getEmail());
        if (UserRole.DRIVER.equals(user.getRole())) {
            validateDriverHasVehicle(user);
        }
    }
    private void validateInstitutionalEmail(String email) {
        if (!email.endsWith(INSTITUTIONAL_DOMAIN)) {
            throw new IllegalArgumentException(
                "El email debe pertenecer al dominio institucional " + INSTITUTIONAL_DOMAIN
            );
        }
    }
    private void validateEmailNotTaken(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException(
                "El email ya se encuentra registrado en el sistema"
            );
        }
    }
    private void validateDriverHasVehicle(User user) {
        if (user.getVehicle() == null
                || user.getVehicle().getLicensePlate() == null
                || user.getVehicle().getLicensePlate().isBlank()) {
            throw new IllegalArgumentException(
                "Un conductor debe registrar la informacion de su vehiculo (licensePlate, brand, model)"
            );
        }
    }
}
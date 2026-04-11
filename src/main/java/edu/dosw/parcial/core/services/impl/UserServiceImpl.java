package edu.dosw.parcial.core.services.impl;
import edu.dosw.parcial.core.models.User;
import edu.dosw.parcial.core.services.UserService;
import edu.dosw.parcial.core.validators.UserValidator;
import edu.dosw.parcial.persistence.entities.UserEntity;
import edu.dosw.parcial.persistence.entities.VehicleEntity;
import edu.dosw.parcial.persistence.mappers.UserPersistenceMapper;
import edu.dosw.parcial.persistence.mappers.VehiclePersistenceMapper;
import edu.dosw.parcial.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserPersistenceMapper userPersistenceMapper;
    private final VehiclePersistenceMapper vehiclePersistenceMapper;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User register(User user) {
        userValidator.validateForRegistration(user);
        String plainPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        user.setPasswordHash(hashedPassword);
        UserEntity userEntity = userPersistenceMapper.toEntity(user);
        if (user.getVehicle() != null) {
            VehicleEntity vehicleEntity = vehiclePersistenceMapper.toEntity(user.getVehicle());
            vehicleEntity.setUser(userEntity);
            userEntity.setVehicle(vehicleEntity);
        }
        UserEntity saved = userRepository.save(userEntity);
        return userPersistenceMapper.toDomain(saved);
    }
}

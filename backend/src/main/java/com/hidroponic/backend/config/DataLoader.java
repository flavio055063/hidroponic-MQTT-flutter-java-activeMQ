package com.hidroponic.backend.config;

import com.hidroponic.backend.model.Equipment;
import com.hidroponic.backend.model.Role;
import com.hidroponic.backend.model.User;
import com.hidroponic.backend.repository.EquipmentRepository;
import com.hidroponic.backend.repository.UserRepository;
import java.util.Collections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, EquipmentRepository equipmentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            Equipment equipment = new Equipment("Demo Kit");
            equipmentRepository.save(equipment);
            User admin = new User("admin", passwordEncoder.encode("admin"), Collections.singleton(Role.ADMIN));
            admin.setEquipment(equipment);
            equipment.setOwner(admin);
            userRepository.save(admin);
            equipmentRepository.save(equipment);
        }
    }
}

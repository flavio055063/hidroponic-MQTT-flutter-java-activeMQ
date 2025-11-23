package com.hidroponic.backend.controller;

import com.hidroponic.backend.model.Equipment;
import com.hidroponic.backend.model.User;
import com.hidroponic.backend.repository.EquipmentRepository;
import com.hidroponic.backend.repository.UserRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    public AdminController(UserRepository userRepository, EquipmentRepository equipmentRepository) {
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> listEquipment() {
        return ResponseEntity.ok(equipmentRepository.findAll());
    }
}

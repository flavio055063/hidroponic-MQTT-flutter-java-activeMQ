package com.hidroponic.backend.repository;

import com.hidroponic.backend.model.Equipment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByOwnerUsername(String username);
}

package com.hidroponic.backend.repository;

import com.hidroponic.backend.model.SensorReading;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findTop30ByEquipmentIdOrderByRecordedAtDesc(Long equipmentId);

    @Query("SELECT r FROM SensorReading r WHERE r.equipment.id = :equipmentId AND r.recordedAt BETWEEN :start AND :end ORDER BY r.recordedAt DESC")
    List<SensorReading> findBetween(@Param("equipmentId") Long equipmentId, @Param("start") Instant start, @Param("end") Instant end);

    SensorReading findTop1ByEquipmentIdOrderByRecordedAtDesc(Long equipmentId);
}

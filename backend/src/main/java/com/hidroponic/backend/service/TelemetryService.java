package com.hidroponic.backend.service;

import com.hidroponic.backend.dto.TelemetryRequest;
import com.hidroponic.backend.dto.TelemetryResponse;
import com.hidroponic.backend.model.Equipment;
import com.hidroponic.backend.model.SensorReading;
import com.hidroponic.backend.repository.SensorReadingRepository;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelemetryService {

    private final SensorReadingRepository sensorReadingRepository;
    private final AlertService alertService;
    private final Map<Long, Deque<SensorReading>> phTemperatureBuffers = new ConcurrentHashMap<>();

    public TelemetryService(SensorReadingRepository sensorReadingRepository, AlertService alertService) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.alertService = alertService;
    }

    @Transactional
    public SensorReading recordTelemetry(Equipment equipment, TelemetryRequest request) {
        SensorReading reading = new SensorReading(
                equipment,
                request.getPh(),
                request.getTemperature(),
                request.getLuminosity(),
                request.getTds(),
                equipment.getTargetPhMin(),
                equipment.getTargetPhMax());
        SensorReading saved = sensorReadingRepository.save(reading);
        enqueueBuffer(saved);
        alertService.checkSeverePhDeviation(saved);
        return saved;
    }

    public TelemetryResponse latest(Long equipmentId) {
        SensorReading reading = sensorReadingRepository.findTop1ByEquipmentIdOrderByRecordedAtDesc(equipmentId);
        if (reading == null) {
            return null;
        }
        return new TelemetryResponse(
                reading.getPh(),
                reading.getTemperature(),
                reading.getLuminosity(),
                reading.getTds(),
                reading.getTargetPhMin(),
                reading.getTargetPhMax(),
                reading.getRecordedAt());
    }

    public java.util.List<SensorReading> last30(Long equipmentId) {
        return sensorReadingRepository.findTop30ByEquipmentIdOrderByRecordedAtDesc(equipmentId);
    }

    private void enqueueBuffer(SensorReading reading) {
        Deque<SensorReading> deque = phTemperatureBuffers.computeIfAbsent(reading.getEquipment().getId(), id -> new ArrayDeque<>());
        deque.addLast(reading);
        while (deque.size() > 30) {
            deque.removeFirst();
        }
    }

    public Optional<Deque<SensorReading>> getBuffer(Long equipmentId) {
        return Optional.ofNullable(phTemperatureBuffers.get(equipmentId));
    }
}

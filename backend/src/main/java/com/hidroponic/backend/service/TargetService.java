package com.hidroponic.backend.service;

import com.hidroponic.backend.dto.TargetUpdateRequest;
import com.hidroponic.backend.model.Equipment;
import com.hidroponic.backend.repository.EquipmentRepository;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class TargetService {
    private final EquipmentRepository equipmentRepository;
    private final List<SseEmitter> targetEmitters = new CopyOnWriteArrayList<>();

    public TargetService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional
    public Equipment updateTarget(Long equipmentId, TargetUpdateRequest request) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));
        equipment.setTargetPhMin(request.getTargetMin());
        equipment.setTargetPhMax(request.getTargetMax());
        Equipment saved = equipmentRepository.save(equipment);
        broadcastTargetChange(saved);
        return saved;
    }

    public SseEmitter subscribeToTargets() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        targetEmitters.add(emitter);
        emitter.onCompletion(() -> targetEmitters.remove(emitter));
        emitter.onTimeout(() -> targetEmitters.remove(emitter));
        return emitter;
    }

    public void broadcastTargetChange(Equipment equipment) {
        List<SseEmitter> dead = new CopyOnWriteArrayList<>();
        targetEmitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("target").data(equipment.getTargetPhMin() + "," + equipment.getTargetPhMax()));
            } catch (IOException e) {
                dead.add(emitter);
            }
        });
        targetEmitters.removeAll(dead);
    }
}

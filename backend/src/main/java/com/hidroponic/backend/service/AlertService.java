package com.hidroponic.backend.service;

import com.hidroponic.backend.model.SensorReading;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class AlertService {
    private static final Logger log = LoggerFactory.getLogger(AlertService.class);
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void checkSeverePhDeviation(SensorReading reading) {
        double min = reading.getTargetPhMin();
        double max = reading.getTargetPhMax();
        double tolerance = (max - min) * 0.5;
        double ph = reading.getPh();
        if (ph < min - tolerance || ph > max + tolerance) {
            broadcastAlert("Severe pH deviation detected", ph);
        }
    }

    public SseEmitter addEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    public void broadcastAlert(String message, double ph) {
        List<SseEmitter> dead = new CopyOnWriteArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("alert").data(message + " (pH=" + ph + ")"));
            } catch (IOException e) {
                dead.add(emitter);
            }
        });
        emitters.removeAll(dead);
        log.warn("{} (pH={})", message, ph);
    }
}

package com.hidroponic.backend.service;

import com.hidroponic.backend.dto.ReportRequest;
import com.hidroponic.backend.dto.ReportResponse;
import com.hidroponic.backend.model.SensorReading;
import com.hidroponic.backend.repository.SensorReadingRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private final SensorReadingRepository sensorReadingRepository;

    public ReportService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public ReportResponse buildReport(Long equipmentId, ReportRequest request) {
        List<SensorReading> readings = sensorReadingRepository.findBetween(equipmentId, request.getStart(), request.getEnd());
        if (readings.isEmpty()) {
            return new ReportResponse(0, 0, 0, 0);
        }
        double avgPh = readings.stream().mapToDouble(SensorReading::getPh).average().orElse(0);
        double avgTemp = readings.stream().mapToDouble(SensorReading::getTemperature).average().orElse(0);
        double avgLum = readings.stream().mapToDouble(SensorReading::getLuminosity).average().orElse(0);
        double avgTds = readings.stream().mapToDouble(SensorReading::getTds).average().orElse(0);
        return new ReportResponse(avgPh, avgTemp, avgLum, avgTds);
    }
}

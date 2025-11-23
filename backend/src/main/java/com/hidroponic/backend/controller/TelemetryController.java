package com.hidroponic.backend.controller;

import com.hidroponic.backend.dto.ReportRequest;
import com.hidroponic.backend.dto.ReportResponse;
import com.hidroponic.backend.dto.TelemetryRequest;
import com.hidroponic.backend.dto.TelemetryResponse;
import com.hidroponic.backend.model.Equipment;
import com.hidroponic.backend.model.User;
import com.hidroponic.backend.service.ActionLogService;
import com.hidroponic.backend.service.ReportService;
import com.hidroponic.backend.service.TelemetryService;
import com.hidroponic.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipment")
public class TelemetryController {

    private final TelemetryService telemetryService;
    private final UserService userService;
    private final ReportService reportService;
    private final ActionLogService actionLogService;

    public TelemetryController(TelemetryService telemetryService, UserService userService, ReportService reportService,
                               ActionLogService actionLogService) {
        this.telemetryService = telemetryService;
        this.userService = userService;
        this.reportService = reportService;
        this.actionLogService = actionLogService;
    }

    @PostMapping("/{equipmentId}/telemetry")
    public ResponseEntity<TelemetryResponse> ingest(@PathVariable Long equipmentId, @Valid @RequestBody TelemetryRequest request,
                                                    Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        if (user.getEquipment() == null || !user.getEquipment().getId().equals(equipmentId)) {
            return ResponseEntity.status(403).build();
        }
        Equipment equipment = user.getEquipment();
        var saved = telemetryService.recordTelemetry(equipment, request);
        actionLogService.log(user.getUsername(), "ingest", "Telemetry recorded");
        return ResponseEntity.ok(new TelemetryResponse(saved.getPh(), saved.getTemperature(), saved.getLuminosity(), saved.getTds(),
                saved.getTargetPhMin(), saved.getTargetPhMax(), saved.getRecordedAt()));
    }

    @GetMapping("/{equipmentId}/telemetry/latest")
    public ResponseEntity<TelemetryResponse> latest(@PathVariable Long equipmentId, Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        if (user.getEquipment() == null || !user.getEquipment().getId().equals(equipmentId)) {
            return ResponseEntity.status(403).build();
        }
        TelemetryResponse response = telemetryService.latest(equipmentId);
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{equipmentId}/report")
    public ResponseEntity<ReportResponse> report(@PathVariable Long equipmentId, @Valid @RequestBody ReportRequest request,
                                                 Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        if (user.getEquipment() == null || !user.getEquipment().getId().equals(equipmentId)) {
            return ResponseEntity.status(403).build();
        }
        ReportResponse response = reportService.buildReport(equipmentId, request);
        actionLogService.log(user.getUsername(), "report", "Report generated");
        return ResponseEntity.ok(response);
    }
}

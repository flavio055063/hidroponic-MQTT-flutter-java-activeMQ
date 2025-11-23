package com.hidroponic.backend.controller;

import com.hidroponic.backend.dto.TargetUpdateRequest;
import com.hidroponic.backend.model.Equipment;
import com.hidroponic.backend.model.User;
import com.hidroponic.backend.service.ActionLogService;
import com.hidroponic.backend.service.TargetService;
import com.hidroponic.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/targets")
public class TargetController {
    private final TargetService targetService;
    private final UserService userService;
    private final ActionLogService actionLogService;

    public TargetController(TargetService targetService, UserService userService, ActionLogService actionLogService) {
        this.targetService = targetService;
        this.userService = userService;
        this.actionLogService = actionLogService;
    }

    @PostMapping("/{equipmentId}")
    public ResponseEntity<Equipment> updateTarget(@PathVariable Long equipmentId, @Valid @RequestBody TargetUpdateRequest request,
                                                  Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        if (user.getEquipment() == null || !user.getEquipment().getId().equals(equipmentId)) {
            return ResponseEntity.status(403).build();
        }
        Equipment updated = targetService.updateTarget(equipmentId, request);
        actionLogService.log(user.getUsername(), "target-change", "Updated pH target to " + request.getTargetMin() + "-" + request.getTargetMax());
        return ResponseEntity.ok(updated);
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter targetStream() {
        return targetService.subscribeToTargets();
    }
}

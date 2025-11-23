package com.hidroponic.backend.controller;

import com.hidroponic.backend.dto.AuthRequest;
import com.hidroponic.backend.dto.AuthResponse;
import com.hidroponic.backend.dto.RegisterRequest;
import com.hidroponic.backend.service.ActionLogService;
import com.hidroponic.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final ActionLogService actionLogService;

    public AuthController(UserService userService, ActionLogService actionLogService) {
        this.userService = userService;
        this.actionLogService = actionLogService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        actionLogService.log(request.getUsername(), "register", "Registered user and equipment");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.authenticate(request);
        actionLogService.log(request.getUsername(), "login", "User logged in");
        return ResponseEntity.ok(response);
    }
}

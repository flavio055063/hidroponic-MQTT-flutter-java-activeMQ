package com.hidroponic.backend.service;

import com.hidroponic.backend.dto.AuthRequest;
import com.hidroponic.backend.dto.AuthResponse;
import com.hidroponic.backend.dto.RegisterRequest;
import com.hidroponic.backend.model.Equipment;
import com.hidroponic.backend.model.Role;
import com.hidroponic.backend.model.User;
import com.hidroponic.backend.repository.EquipmentRepository;
import com.hidroponic.backend.repository.UserRepository;
import com.hidroponic.backend.config.JwtService;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, EquipmentRepository equipmentRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        Equipment equipment = new Equipment(Optional.ofNullable(request.getEquipmentName()).orElse("HydroKit"));
        equipmentRepository.save(equipment);

        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), Collections.singleton(Role.USER));
        user.setEquipment(equipment);
        equipment.setOwner(user);
        userRepository.save(user);
        equipmentRepository.save(equipment);
        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token, equipment.getId(), user.getUsername(), false);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtService.generateToken(user.getUsername());
        boolean admin = user.getRoles().contains(Role.ADMIN);
        return new AuthResponse(token, user.getEquipment() != null ? user.getEquipment().getId() : null, user.getUsername(), admin);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUsername(username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                user.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).toList());
    }
}

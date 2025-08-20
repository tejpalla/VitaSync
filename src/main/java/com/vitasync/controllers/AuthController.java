package com.vitasync.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitasync.dto.AuthRequest;
import com.vitasync.dto.AuthResponse;
import com.vitasync.dto.RegisterRequest;
import com.vitasync.entity.User;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.UserRole;
import com.vitasync.repository.UserRepository;
import com.vitasync.security.JwtTokenUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Authentication Controller for VitaSync Blood Bank Management System
 * 
 * API Endpoints:
 * 1. POST /api/auth/register - Register new user
 *    - Returns 201 (Created) with JWT token on success
 *    - Returns 409 (Conflict) if email already exists
 * 
 * 2. POST /api/auth/login - Login existing user  
 *    - Returns 200 (OK) with JWT token on success
 *    - Returns 401 (Unauthorized) for invalid credentials
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AuthResponse(null, null, null, "ERROR: Email already registered. Please use a different email or try logging in."));
        }
        
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password
        newUser.setPhone(request.getPhone());
        newUser.setBloodType(request.getBloodType() != null ? 
            BloodType.valueOf(request.getBloodType()) : null);
        newUser.setRole(request.getRole() != null ? 
            UserRole.valueOf(request.getRole()) : UserRole.DONOR);
        
        // Set default location if not provided (required fields)
        newUser.setLongitude(request.getLongitude() != null ? request.getLongitude() : 0.0);
        newUser.setLatitude(request.getLatitude() != null ? request.getLatitude() : 0.0);
        
        User savedUser = userRepository.save(newUser);
        
        // Auto-login after registration
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(userDetails);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new AuthResponse(token, savedUser.getEmail(), savedUser.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            
            User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getRole().name()));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(null, null, null, "ERROR: Invalid email or password. Please check your credentials."));
        }
    }
}

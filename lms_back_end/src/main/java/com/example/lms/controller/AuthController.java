package com.example.lms.controller;

import com.example.lms.dto.auth.JwtResponse;
import com.example.lms.dto.auth.LoginRequest;
import com.example.lms.dto.auth.RegisterRequest;
import com.example.lms.service.AuthService;
import com.example.lms.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600) // ඔබට CORS සැකසුම් application.yml වලින් පාලනය කළ හැක
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = authService.authenticateUser(loginRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Note: You might need to fetch the User entity to get the ID for JwtResponse
        // For simplicity, we are assuming UserDetails contains sufficient info or fetching by username is fine.
        // A better approach would be to have a custom UserDetails implementation.
        // For now, let's use a dummy ID.
        Long userId = userDetailsService.loadUserByUsername(loginRequest.getUsername()).hashCode() * 1L; // Placeholder

        return ResponseEntity.ok(new JwtResponse(jwt, userId, userDetails.getUsername(), userDetails.getUsername(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }
}

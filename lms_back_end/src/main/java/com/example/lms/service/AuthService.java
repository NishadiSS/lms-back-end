package com.example.lms.service;

import com.example.lms.dto.auth.LoginRequest;
import com.example.lms.dto.auth.RegisterRequest;
import com.example.lms.entity.ERole;
import com.example.lms.entity.Role;
import com.example.lms.entity.User;
import com.example.lms.repository.RoleRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <<-- මෙය එකතු කර තිබිය යුතුය

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional // <<-- මෙය එකතු කර තිබිය යුතුය
    public void registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                // <<<<<<< මෙහි වෙනසක් කරන්න >>>>>>>
                switch (role.toUpperCase()) { // <<-- මෙහිදී role.toUpperCase() ලෙස වෙනස් කරන්න
                    case "ADMIN": // <<-- "ADMIN" ලෙස වෙනස් කරන්න
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "INSTRUCTOR": // <<-- "INSTRUCTOR" ලෙස වෙනස් කරන්න
                        Role modRole = roleRepository.findByName(ERole.ROLE_INSTRUCTOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    case "STUDENT": // <<-- "STUDENT" ලෙස වෙනස් කරන්න (නිවැරදි match එකක් සඳහා)
                    default: // Default case for any unrecognised role, or if "STUDENT" is explicitly chosen
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                        break; // <<-- default case එකට break එකක් එකතු කරන්න
                }
                // <<<<<<< මෙහි වෙනසක් කරන්න >>>>>>>
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
    }
}
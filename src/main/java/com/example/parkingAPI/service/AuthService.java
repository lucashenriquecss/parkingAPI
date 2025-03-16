package com.example.parkingAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.parkingAPI.dto.AuthRequest;
import com.example.parkingAPI.dto.SwitchProfileRequest;
import com.example.parkingAPI.dto.AuthResponse;
import com.example.parkingAPI.dto.SwitchProfileResponse;
import com.example.parkingAPI.entity.Profile;
import com.example.parkingAPI.entity.User;
import com.example.parkingAPI.entity.enums.Role;
import com.example.parkingAPI.infrastructure.exception.BadRequestException;
import com.example.parkingAPI.infrastructure.exception.ForbiddenException;
import com.example.parkingAPI.infrastructure.exception.NotFoundException;
import com.example.parkingAPI.infrastructure.exception.UnauthorizedException;
import com.example.parkingAPI.infrastructure.security.JwtUtil;
import com.example.parkingAPI.repository.UserRepository;
import com.example.parkingAPI.repository.ProfileRepository;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ForbiddenException("Email já cadastrado!");
        }
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); 
        Profile profile = new Profile();
        profile.setRole(Role.COMMON);
        profile.setName("USER NORMAL");
        profile.setUser(user);

        user.setProfiles(Collections.singletonList(profile));

        return userRepository.save(user);
    }

    public  void exception() {
        throw new NotFoundException("Email já cadastrado!"); 
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        String token = jwtUtil.generateTokenLogin(user.getEmail(), user.getId());
        return new AuthResponse(token);
    }

    public SwitchProfileResponse selectProfile(String token, SwitchProfileRequest request) {
        if (!jwtUtil.validateToken(token.replace("Bearer ", ""))) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        String idUser = jwtUtil.extractUserId(token.replace("Bearer ", ""));

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new NotFoundException("Profile not found"));

        if (!user.getProfiles().contains(profile)) {
            throw new BadRequestException("Invalid profile selection");
        }

        String newToken = jwtUtil.generateToken(user.getId(), profile.getId(), profile.getRole().name());

        return new SwitchProfileResponse(newToken, profile.getRole().name());
    }
}
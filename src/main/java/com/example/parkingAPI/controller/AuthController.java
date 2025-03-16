package com.example.parkingAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.parkingAPI.dto.AuthRequest;
import com.example.parkingAPI.dto.AuthResponse;
import com.example.parkingAPI.dto.SwitchProfileRequest;
import com.example.parkingAPI.dto.SwitchProfileResponse;
import com.example.parkingAPI.entity.User;

import com.example.parkingAPI.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

   @Autowired
    private AuthService authService;

 
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/exception")
    public ResponseEntity<?> getVehicles(@RequestBody Object body) {
        authService.exception();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile")
    public ResponseEntity<SwitchProfileResponse> selectProfile(
        @RequestHeader("Authorization") String token, 
        @RequestBody SwitchProfileRequest request
    ) {
        SwitchProfileResponse response = authService.selectProfile(token, request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody AuthRequest request) {
        User user = authService.registerUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(user);
    }

}

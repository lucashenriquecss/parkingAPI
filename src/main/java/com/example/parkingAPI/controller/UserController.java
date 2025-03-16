package com.example.parkingAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parkingAPI.dto.CreateVehicleRequest;
import com.example.parkingAPI.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profiles")
    public ResponseEntity<?> getProfiles(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userService.getProfiles(userId));
    }

    @GetMapping("/vehicles")
    public ResponseEntity<?> getVehicles(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userService.getVehicles(userId));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<?> getVehicles(Authentication authentication, @RequestBody CreateVehicleRequest request) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.postVehicle(request,userId));
    }

    
}

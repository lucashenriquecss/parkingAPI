package com.example.parkingAPI.controller;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parkingAPI.dto.CreateBusinessRequest;
import com.example.parkingAPI.dto.CreateEmployeeRequest;
import com.example.parkingAPI.dto.CreateParkingRequest;
import com.example.parkingAPI.entity.Profile;
import com.example.parkingAPI.service.ProfileService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;


    @PostMapping("/business")
    public ResponseEntity<?> createProfileBusiness(Authentication authentication,@RequestBody CreateBusinessRequest request) {
        String userId = (String) authentication.getPrincipal();
        Profile profile = profileService.createBusinessProfile(userId, request);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/{profileId}/employee/parking/{parkingId}")
    public ResponseEntity<?> createProfilEmployee(@PathVariable String profileId,@PathVariable String parkingId,@RequestHeader("Authorization") String token,@RequestBody CreateEmployeeRequest request) {
        Profile profile = profileService.createEmployeeProfile(profileId,parkingId,token,request);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}/parking")
    public  ResponseEntity<?> addParking(@PathVariable String id, @RequestBody CreateParkingRequest entity,@RequestHeader("Authorization") String token) {
        Profile updatedProfile = profileService.addParkingToProfile(id, entity,token);
        return ResponseEntity.ok(updatedProfile);
    }

}


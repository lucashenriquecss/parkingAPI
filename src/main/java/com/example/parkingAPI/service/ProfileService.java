package com.example.parkingAPI.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.parkingAPI.dto.CreateBusinessRequest;
import com.example.parkingAPI.dto.CreateEmployeeRequest;
import com.example.parkingAPI.dto.CreateParkingRequest;
import com.example.parkingAPI.entity.Parking;
import com.example.parkingAPI.entity.Profile;
import com.example.parkingAPI.entity.User;
import com.example.parkingAPI.entity.enums.Role;
import com.example.parkingAPI.infrastructure.exception.ForbiddenException;
import com.example.parkingAPI.infrastructure.exception.NotFoundException;
import com.example.parkingAPI.infrastructure.security.JwtUtil;
import com.example.parkingAPI.repository.ParkingRepository;
import com.example.parkingAPI.repository.ProfileRepository;
import com.example.parkingAPI.repository.UserRepository;

@Service
public class ProfileService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    // @Autowired
    // private BCryptPasswordEncoder passwordEncoder;

    // @Autowired
    // private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public Profile createBusinessProfile(String userId, CreateBusinessRequest request) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        
        List<Profile> existingBusinessProfiles = profileRepository.findByRole(Role.BUSINESS);
        if (!existingBusinessProfiles.isEmpty()) {
            throw new ForbiddenException("Perfil business já existe!");
        }

        Parking parking = Parking.builder()
            .name(request.getParkingName())
            .address(request.getParkingAddress())
            .phone(request.getParkingPhone())
            .cnpj(request.getParkingCnpj())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .build();
        
        parking = parkingRepository.save(parking);
        
        Profile profile = new Profile();
        profile.setRole(Role.BUSINESS);
        profile.setName(request.getName());
        profile.setUser(user);
        profile.getParkings().add(parking);
        
        return profileRepository.save(profile);
    }

  public Profile createEmployeeProfile(String profileId,String parkingId,String token, CreateEmployeeRequest request) {
    String profileIdFromToken = jwtUtil.extractProfileId(token.replace("Bearer ", ""));

    Profile profileExist = profileRepository.findById(profileId)
        .orElseThrow(() -> new NotFoundException("Perfil não encontrado"));

    if (!profileExist.getRole().equals(Role.BUSINESS)) {
        throw new ForbiddenException("Apenas perfis BUSINESS podem ter estacionamentos vinculados");
    }

    if (!profileExist.getId().equals(profileIdFromToken)) {
        throw new ForbiddenException("Este ID não pertence ao seu usuário");
    }
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    Parking parking = parkingRepository.findById(parkingId)
        .orElseThrow(() -> new NotFoundException("Estacionamento não encontrado"));

    boolean exists = profileRepository.findEmployeeProfileByUserAndParking(request.getUserId(), parking).isPresent();
    
    if (exists) {
        throw new ForbiddenException("O usuário já possui um perfil de funcionário para este estacionamento.");
    }

    Profile profile = new Profile();
    profile.setRole(Role.EMPLOYEE);
    profile.setName("Funcionário - " + parking.getName()); // Defina um nome adequado
    profile.setUser(user);
    profile.setParkings(new HashSet<>()); // Inicializar o conjunto de estacionamentos
    profile.getParkings().add(parking);

    return profileRepository.save(profile);
}

    public Profile addParkingToProfile(String profileId, CreateParkingRequest entity,String token) {
        String profileIdFromToken = jwtUtil.extractProfileId(token.replace("Bearer ", ""));

        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new NotFoundException("Perfil não encontrado"));

        if (!profile.getRole().equals(Role.BUSINESS)) {
            throw new ForbiddenException("Apenas perfis BUSINESS podem ter estacionamentos vinculados");
        }

        if (!profile.getId().equals(profileIdFromToken)) {
            throw new ForbiddenException("Este ID não pertence ao seu usuário");
        }

        Parking parking = Parking.builder()
            .name(entity.getParkingName())
            .address(entity.getParkingAddress())
            .phone(entity.getParkingPhone())
            .cnpj(entity.getParkingCnpj())
            .latitude(entity.getLatitude())
            .longitude(entity.getLongitude())
            .build();
        
        parking = parkingRepository.save(parking);
        profile.getParkings().add(parking);
        return profileRepository.save(profile);
    }
}

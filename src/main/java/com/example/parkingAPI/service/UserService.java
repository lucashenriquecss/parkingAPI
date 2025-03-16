package com.example.parkingAPI.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.parkingAPI.dto.CreateVehicleRequest;
import com.example.parkingAPI.entity.Profile;
import com.example.parkingAPI.entity.User;
import com.example.parkingAPI.entity.Vehicle;
import com.example.parkingAPI.infrastructure.exception.BadRequestException;
import com.example.parkingAPI.repository.ProfileRepository;
import com.example.parkingAPI.repository.UserRepository;
import com.example.parkingAPI.repository.VehicleRepository;

@Service
public class UserService {

    private final ProfileRepository profileRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository,VehicleRepository vehicleRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.vehicleRepository =vehicleRepository;
    }

    public List<Profile> getProfiles(String userId) {
        return profileRepository.findByUserId(userId);
    }

    public List<Vehicle> getVehicles(String userId) {
        return vehicleRepository.findByUserId(userId);
    }
    
    public Vehicle postVehicle(CreateVehicleRequest request, String userId ) {

        User user = userRepository.findById(userId)
        .orElseThrow(() -> new BadRequestException("Usuário não encontrado."));

        Optional<Vehicle> existingVehicle = vehicleRepository.findByUserIdAndPlate(userId, request.getPlate());
        if (existingVehicle.isPresent()) {
            throw new BadRequestException("Já existe um veículo com esta placa para este usuário.");
        }    

        Vehicle newVehicle = new Vehicle();
        newVehicle.setUser(user);
        newVehicle.setType(request.getType()!= null ? request.getType().orElse(null) : null);    
        newVehicle.setPlate(request.getPlate());
        newVehicle.setVehicleName(request.getVehicleName()!= null ? request.getVehicleName().orElse(null) : null);
        newVehicle.setVehicleAge(request.getVehicleAge() != null ? request.getVehicleAge().orElse(null) : null);
        newVehicle.setVehicleColor(request.getVehicleColor()!= null ? request.getVehicleColor().orElse(null) : null);
        newVehicle.setObservation(request.getObservation() != null ? request.getObservation().orElse(null) : null);

        return vehicleRepository.save(newVehicle);
    }
}

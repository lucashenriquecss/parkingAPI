package com.example.parkingAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.parkingAPI.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByUserId(String userId);
    Optional<Vehicle> findByUserIdAndPlate(String userId, String plate);
    Optional<Vehicle> findByPlate(String plate);


}
package com.example.parkingAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.parkingAPI.entity.Parking;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, String> {
    Page<Parking> findByNameContainingIgnoreCase(String name, Pageable pageable);

}

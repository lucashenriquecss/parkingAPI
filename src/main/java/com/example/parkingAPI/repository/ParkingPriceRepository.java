package com.example.parkingAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.parkingAPI.entity.ParkingPrice;
import com.example.parkingAPI.entity.enums.PriceType;

@Repository
public interface ParkingPriceRepository extends JpaRepository<ParkingPrice, String> {
    Optional<ParkingPrice> findByParkingIdAndType(String parkingId, PriceType type);

}
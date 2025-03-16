package com.example.parkingAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.parkingAPI.entity.Parking;
import com.example.parkingAPI.entity.Profile;
import com.example.parkingAPI.entity.enums.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    List<Profile> findByUserId(String userId);
    
    List<Profile> findByRole(Role role);

    @Query("SELECT p FROM profilers p WHERE p.user.id = :userId AND p.role = 'EMPLOYEE' AND :parking MEMBER OF p.parkings")
    Optional<Profile> findEmployeeProfileByUserAndParking(@Param("userId") String userId, @Param("parking") Parking parking);
}

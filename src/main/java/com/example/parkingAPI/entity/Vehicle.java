package com.example.parkingAPI.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Table(name = "vehicles")
@Entity(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Vehicle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = true) 
    private String type;

    @Column(nullable = false, unique = true, length = 7) 
    private String plate;

    @Column(nullable = true, name = "vehicle_name") 
    private String vehicleName;

    @Column(nullable = true, name = "vehicle_age") 
    private String vehicleAge;

    @Column(nullable = true, name = "vehicle_color") 
    private String vehicleColor;

    @Column(nullable = true) 
    private String observation;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParkingTicket> ParkingTickets;
}

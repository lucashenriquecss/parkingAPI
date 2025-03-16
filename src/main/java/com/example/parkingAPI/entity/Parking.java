package com.example.parkingAPI.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity(name = "parkings")
@Table(name = "parkings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Parking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String phone;
    
    @Column(nullable = true, unique = true)
    private String cnpj;

    @Column(nullable= true, name="total_vacancies")
    private Integer totalVacancies;

    @Column(nullable= true, name="vacancies_filled")
    private Integer vacanciesFilled;

    @Column(nullable = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = true, unique = true, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = true, unique = true, precision = 10, scale = 7)
    private BigDecimal longitude;


    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParkingPrice> parkingPrices;

    @ManyToMany
    @JoinTable(
        name = "parking_profiles",
        joinColumns = @JoinColumn(name = "parking_id"),
        inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    @JsonIgnore 
    private Set<Profile> profiles;



    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParkingTicket> ParkingTickets;

}

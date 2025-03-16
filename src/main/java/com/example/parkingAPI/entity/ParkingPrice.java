package com.example.parkingAPI.entity;
import com.example.parkingAPI.entity.enums.PriceType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Table(name = "parking_prices")
@Entity(name = "parking_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class ParkingPrice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private PriceType type;

    @Column(nullable = false)
    private Integer time; 

    @Column(precision = 10, scale = 2,nullable = false)
    private BigDecimal value; 

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    @JsonIgnore
    private Parking parking;
}

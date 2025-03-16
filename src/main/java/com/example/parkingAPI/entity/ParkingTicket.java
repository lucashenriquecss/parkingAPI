package com.example.parkingAPI.entity;
import java.math.BigDecimal;
import java.util.Date;

import com.example.parkingAPI.entity.enums.PriceType;
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
@Table(name = "parking_tickets")
@Entity(name = "parking_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class ParkingTicket extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
 
    @Temporal(TemporalType.TIMESTAMP) 
    @Column(nullable = false, name = "check_in")
    private Date checkIn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private PriceType type = PriceType.HOURLY;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true, name="check_out")
    private Date checkOut;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    @JsonIgnore
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    @JsonIgnore
    private Parking parking;

}

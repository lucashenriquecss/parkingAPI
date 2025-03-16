package com.example.parkingAPI.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.parkingAPI.dto.CreateParkingPriceRequest;
import com.example.parkingAPI.dto.CreateTicketParkingRequest;
import com.example.parkingAPI.dto.UpdateParkingTicketRequest;
import com.example.parkingAPI.entity.Parking;
import com.example.parkingAPI.entity.ParkingPrice;
import com.example.parkingAPI.entity.ParkingTicket;
import com.example.parkingAPI.entity.Vehicle;
import com.example.parkingAPI.entity.enums.PriceType;
import com.example.parkingAPI.entity.enums.VehicleType;
import com.example.parkingAPI.infrastructure.security.JwtUtil;
import com.example.parkingAPI.repository.ParkingPriceRepository;
import com.example.parkingAPI.repository.ParkingRepository;
import com.example.parkingAPI.repository.ParkingTicketRepository;
import com.example.parkingAPI.repository.VehicleRepository;
import com.example.parkingAPI.infrastructure.exception.ForbiddenException;
import com.example.parkingAPI.infrastructure.exception.NotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ParkingService {
    @Autowired
    private ParkingPriceRepository parkingPriceRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingTicketRepository parkingTicketRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public ParkingPrice createPriceParking(String parkingId, CreateParkingPriceRequest request,String token) {
        String profileIdFromToken = jwtUtil.extractProfileId(token.replace("Bearer ", ""));

        Optional<Parking> optionalParking = parkingRepository.findById(parkingId);

        if (optionalParking.isEmpty()) {
            throw new NotFoundException("Estacionamento não encontrado para o ID: " + parkingId);
        }

        Parking parking = optionalParking.get();

        boolean hasAccess = parking.getProfiles().stream()
        .anyMatch(profile -> profile.getId().equals(profileIdFromToken));

        if (!hasAccess) {
            throw new ForbiddenException("Você não tem permissão para inserir este preço de estacionamento.");
        }

        ParkingPrice parkingPrice = ParkingPrice.builder()
            .time(request.getTime())
            .value(request.getValue())
            .parking(parking)
            .build();

        return parkingPriceRepository.save(parkingPrice);
    }

    public ParkingTicket createTicketParking(String parkingId, CreateTicketParkingRequest request, String token) {
        // String profileIdFromToken = jwtUtil.extractProfileId(token.replace("Bearer ", ""));
        
        Optional<Parking> optionalParking = parkingRepository.findById(parkingId);
        if (optionalParking.isEmpty()) {
            throw new NotFoundException("Estacionamento não encontrado para o ID: " + parkingId);
        }
        
        Parking parking = optionalParking.get();
        
        Vehicle vehicle;
        Optional<Vehicle> optionalVehicle = vehicleRepository.findByPlate(request.getPlate());
        
        if (optionalVehicle.isEmpty()) {
            Vehicle newVehicle = new Vehicle();
            newVehicle.setPlate(request.getPlate());
            vehicle = vehicleRepository.save(newVehicle);
        } else {
            vehicle = optionalVehicle.get();
        }
        
        Date now = new Date(); 
        
        ParkingTicket parkingTicket = ParkingTicket.builder()
            .checkIn(now) 
            .vehicle(vehicle)
            .parking(parking)
            .build();
        
        return parkingTicketRepository.save(parkingTicket);
    }

    public ParkingPrice updatePriceParking(String parkingPriceId, CreateParkingPriceRequest request, String token) {
        String profileIdFromToken = jwtUtil.extractProfileId(token.replace("Bearer ", ""));
    
        Optional<ParkingPrice> optionalPrice = parkingPriceRepository.findById(parkingPriceId);
    
        if (optionalPrice.isEmpty()) {
            throw new NotFoundException("Estacionamento não encontrado para o ID: " + parkingPriceId);
        }
    
        ParkingPrice parkingPrice = optionalPrice.get();
        
        boolean hasAccess = parkingPrice.getParking().getProfiles().stream()
        .anyMatch(profile -> profile.getId().equals(profileIdFromToken));

        if (!hasAccess) {
            throw new ForbiddenException("Você não tem permissão para atualizar este preço de estacionamento.");
        }
    
        parkingPrice.setTime(request.getTime());
        parkingPrice.setValue(request.getValue());
    
        return parkingPriceRepository.save(parkingPrice);
    }
    public List<Parking> getParkings(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Parking> parkingPage;

        if (name == null || name.isEmpty()) {
            parkingPage = parkingRepository.findAll(pageable);
        } else {
            parkingPage = parkingRepository.findByNameContainingIgnoreCase(name, pageable);
        }

        return parkingPage.getContent(); 
    }
    @Transactional
    public Map<String, Object> updateTicket(String id, UpdateParkingTicketRequest updateParkingTicketDto) {
        ParkingTicket ticket = parkingTicketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket não encontrado"));

        if (ticket.getCheckIn() != null && ticket.getCheckOut() != null) {
            throw new ForbiddenException("Ticket já está fechado.");
        }

        if (updateParkingTicketDto.getPlate() == null) {
            throw new ForbiddenException("A placa fornecido não é uma data válida.");
        }

        Parking parking = ticket.getParking();
        if (parking == null) {
            throw new NotFoundException("Estacionamento não encontrado");
        }

        if (parking.getVacanciesFilled() <= 0) {
            throw new ForbiddenException("Não há vagas ocupadas para liberar.");
        }

        parking.setVacanciesFilled(parking.getVacanciesFilled() - 1);
        parkingRepository.save(parking);

        Date now = new Date(); 

        BigDecimal finalValue = calculateParkingFee(ticket.getCheckIn(), now, parking, ticket.getType());

        ticket.setCheckOut(now);
        ticket.setValue(finalValue);
        parkingTicketRepository.save(ticket);

        return Map.of(
            "ticketId", id,
            "updatedValue", finalValue
        );
    }
    public BigDecimal calculateParkingFee(Date checkIn, Date checkOut, Parking parking, PriceType pricingType) {
        if (checkOut.before(checkIn)) {
            throw new ForbiddenException("Check-out não pode ser antes do check-in.");
        }
    
        long timeDifference = (checkOut.getTime() - checkIn.getTime()) / 1000 / 60; // minutos
    
        ParkingPrice price = parkingPriceRepository.findByParkingIdAndType(parking.getId(), pricingType)
                .orElseThrow(() -> new NotFoundException("Preço não encontrado para o tipo " + pricingType));
    
        BigDecimal fee;
        switch (pricingType) {
            case FIXED:
                fee = price.getValue();
                break;
            case HOURLY:
                fee = price.getValue().multiply(BigDecimal.valueOf(Math.ceil((double) timeDifference / price.getTime())));
                break;
            case DAILY:
                fee = price.getValue().multiply(BigDecimal.valueOf(Math.ceil((double) timeDifference / (24 * 60))));
                break;
            case MONTHLY:
                fee = price.getValue().multiply(BigDecimal.valueOf(Math.ceil((double) timeDifference / (30 * 24 * 60))));
                break;
            default:
                throw new ForbiddenException("Tipo de preço desconhecido: " + pricingType);
        }
        return fee;
    }
}

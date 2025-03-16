package com.example.parkingAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.parkingAPI.dto.CreateParkingPriceRequest;
import com.example.parkingAPI.dto.CreateTicketParkingRequest;
import com.example.parkingAPI.dto.UpdateParkingTicketRequest;
import com.example.parkingAPI.entity.ParkingPrice;
import com.example.parkingAPI.entity.ParkingTicket;
import com.example.parkingAPI.service.ParkingService;

@RestController
@RequestMapping("/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;


    @GetMapping()
    public ResponseEntity<?> getProfiles( 
        @RequestParam(required = false, defaultValue = "") String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(parkingService.getParkings(name,page,size));
    }

    @PostMapping("/{id}/price")
    public ResponseEntity<?> postPrices(@PathVariable String id,@RequestBody CreateParkingPriceRequest request,@RequestHeader("Authorization") String token) {
        ParkingPrice parkingPrice = parkingService.createPriceParking(id, request,token);
        return ResponseEntity.ok(parkingPrice);    
    }

    @PutMapping("/price/{id}")
    public ResponseEntity<?> updatePrices(@PathVariable String id, 
                                        @RequestBody CreateParkingPriceRequest request, 
                                        @RequestHeader("Authorization") String token) {
        ParkingPrice parkingPrice = parkingService.updatePriceParking(id, request, token);
        return ResponseEntity.ok(parkingPrice);
    }

    @PostMapping("/{id}/ticket")
    public ResponseEntity<?> postTicket(@PathVariable String id,@RequestBody CreateTicketParkingRequest request,@RequestHeader("Authorization") String token) {
        ParkingTicket parkingTicket = parkingService.createTicketParking(id, request,token);
        return ResponseEntity.ok(parkingTicket);    
    }

    @PutMapping("/ticket/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable String id, 
                                      @RequestBody UpdateParkingTicketRequest request, 
                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(parkingService.updateTicket(id, request));
    }
}


package com.example.parkingAPI.dto;

import java.util.Optional;


public class CreateVehicleRequest {
    private Optional<String> type ;
    private String plate;
    private Optional<String> vehicleName;
    private Optional<String> vehicleAge = Optional.empty(); 
    private Optional<String> vehicleColor;
    private Optional<String> observation = Optional.empty();

  
    public String getPlate() {
        return plate;
    }
    public void setPlate(String plate) {
        this.plate = plate;
    }
  

    public Optional<String> getObservation() {
        return observation != null ? observation : Optional.empty();
    }

    public void setObservation(Optional<String> observation) {
        this.observation = observation != null ? observation : Optional.empty();
    }

    public Optional<String> getType() {
        return  type != null ? type : Optional.empty();
    }

    public void setType(Optional<String> type) {
        this.type = type != null ? type : Optional.empty();;
    }
    public Optional<String> getVehicleName() {
        return vehicleName != null ? vehicleName : Optional.empty();
    }
    public void setVehicleName(Optional<String> vehicleName) {
        this.vehicleName = vehicleName != null ? vehicleName : Optional.empty();
    }
    public Optional<String> getVehicleAge() {
        return vehicleAge != null ? vehicleAge : Optional.empty();
    }
    public void setVehicleAge(Optional<String> vehicleAge) {
        this.vehicleAge = vehicleAge != null ? vehicleAge : Optional.empty();
    }
    public Optional<String>  getVehicleColor() {
        return vehicleColor != null ? vehicleColor : Optional.empty();
    }
    public void setVehicleColor(Optional<String>  vehicleColor) {
        this.vehicleColor = vehicleColor != null ? vehicleColor : Optional.empty();
    }


}

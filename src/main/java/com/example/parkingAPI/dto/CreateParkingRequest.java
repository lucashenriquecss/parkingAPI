package com.example.parkingAPI.dto;

import java.math.BigDecimal;

public class CreateParkingRequest {
    private String parkingName;
    private String parkingAddress;
    private String parkingPhone;
    private String parkingCnpj;
    private BigDecimal latitude;
    private BigDecimal longitude;
    public String getParkingName() {
        return parkingName;
    }
    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }
    public String getParkingAddress() {
        return parkingAddress;
    }
    public void setParkingAddress(String parkingAddress) {
        this.parkingAddress = parkingAddress;
    }
    public String getParkingPhone() {
        return parkingPhone;
    }
    public void setParkingPhone(String parkingPhone) {
        this.parkingPhone = parkingPhone;
    }
    public String getParkingCnpj() {
        return parkingCnpj;
    }
    public void setParkingCnpj(String parkingCnpj) {
        this.parkingCnpj = parkingCnpj;
    }
    public BigDecimal getLatitude() {
        return latitude;
    }
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    public BigDecimal getLongitude() {
        return longitude;
    }
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}

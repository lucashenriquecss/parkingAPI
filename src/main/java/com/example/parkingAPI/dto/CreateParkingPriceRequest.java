package com.example.parkingAPI.dto;

import java.math.BigDecimal;

public class CreateParkingPriceRequest {
    private Integer time;
    private BigDecimal value;
    
    public Integer getTime() {
        return time;
    }
    public void setTime(Integer time) {
        this.time = time;
    }
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

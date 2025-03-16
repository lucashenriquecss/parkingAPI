package com.example.parkingAPI.entity.enums;

public enum PriceType {
    FIXED("fixed"),
    HOURLY("hourly"),
    DAILY("daily"),
    MONTHLY("monthly");

    private final String value;

    PriceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}


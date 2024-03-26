package com.example.demo.model.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExchangeRateResponseDTO {
    private String firstCurrency;
    private String secondCurrency;
    private Timestamp timestamp;
    private String rate;
}

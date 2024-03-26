package com.example.demo.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Data
public class ConvertCurrenciesResponseDto {
    private String firstCurrency;
    private String secondCurrency;
    private BigDecimal amountFirstCurrency;
    private BigDecimal convertedAmount;
    private Timestamp timestamp;
    private String rate;
    private String identifier;
}

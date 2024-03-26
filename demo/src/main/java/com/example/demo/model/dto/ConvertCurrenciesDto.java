package com.example.demo.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConvertCurrenciesDto extends ExchangeRateDto{
    private String firstCurrency;
    private String secondCurrency;
    private BigDecimal amountFirstCurrency;
}

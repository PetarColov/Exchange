package com.example.demo.model.dto;

import com.example.demo.model.enums.ConversionHistoryStatus;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExchangeConversionsHistoryDto {
    private String firstCurrency;
    private String secondCurrency;
    private Timestamp timestamp;
    private ConversionHistoryStatus conversionStatus;
}

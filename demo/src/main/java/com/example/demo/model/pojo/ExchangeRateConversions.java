package com.example.demo.model.pojo;

import com.example.demo.model.enums.ConversionHistoryStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "exchange_rate_conversions")
public class ExchangeRateConversions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "from_currency")
    private String firstCurrency;

    @Column(nullable = false, name = "to_currency")
    private String secondCurrency;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false, name = "amount_first_currency")
    private String amountFirstCurrency;

    @Column(nullable = false, name = "converted_amount")
    private String convertedAmount;

    @Column(nullable = false, name = "rate")
    private String rate;

    @Column(nullable = false, name = "conversion_status")
    private ConversionHistoryStatus conversionStatus;

    @Column(nullable = false, name = "identifier")
    private String identifier;
}

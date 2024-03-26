package com.example.demo.controller;

import com.example.demo.model.dto.*;
import com.example.demo.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExchangeRateController extends AbstractController {

    @Autowired
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @PostMapping("/rate")
    public ExchangeRateResponseDTO getExchangeRate(@RequestBody ExchangeRateDto exchangeRateDto) {
        return exchangeRateService.getExchangeRate(exchangeRateDto);
    }
}

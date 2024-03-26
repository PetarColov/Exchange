package com.example.demo.controller;

import com.example.demo.model.dto.ConvertCurrenciesDto;
import com.example.demo.model.dto.ConvertCurrenciesResponseDto;
import com.example.demo.model.dto.ExchangeConversionsHistoryDto;
import com.example.demo.service.ExchangeConversionsService;
import com.example.demo.service.ExchangeRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConversionController extends AbstractController{

    private final ExchangeConversionsService exchangeConversionsService;

    public ConversionController(ExchangeConversionsService exchangeConversionsService) {
        this.exchangeConversionsService = exchangeConversionsService;
    }

    @PostMapping("/convert")
    public ConvertCurrenciesResponseDto convert(@RequestBody ConvertCurrenciesDto convertCurrenciesDto) {
        return exchangeConversionsService.convert(convertCurrenciesDto);
    }

    @GetMapping("/history")
    public List<ExchangeConversionsHistoryDto> showExchangeHistory(){
        return exchangeConversionsService.showExchangeHistory();
    }
}

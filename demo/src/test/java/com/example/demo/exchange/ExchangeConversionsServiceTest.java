package com.example.demo.exchange;

import com.example.demo.model.dto.ConvertCurrenciesDto;
import com.example.demo.model.dto.ConvertCurrenciesResponseDto;
import com.example.demo.model.repository.ExchangeRateConversionsRepository;
import com.example.demo.model.repository.ExchangeRateHistoryRepository;
import com.example.demo.service.ExchangeConversionsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class ExchangeConversionsServiceTest {
    @Mock
    private ExchangeRateHistoryRepository exchangeRateHistoryRepository;
    private ExchangeRateConversionsRepository exchangeRateConversionsRepository;
    private ModelMapper mapper;
    private ExchangeConversionsService toTest;

    @BeforeEach
    void setUp() {
        toTest = new ExchangeConversionsService(exchangeRateHistoryRepository, exchangeRateConversionsRepository, mapper);
    }

    @Test
    void testConversion(){
        ConvertCurrenciesDto ccDto = new ConvertCurrenciesDto();
        ccDto.setFirstCurrency("USD");
        ccDto.setSecondCurrency("BGN");
        ccDto.setAmountFirstCurrency(new BigDecimal(100));
        ConvertCurrenciesResponseDto convert = toTest.convert(ccDto);
        //Conversion type between two currencies every time is different
    }
}

package com.example.demo.service;

import com.example.demo.model.dto.ConvertCurrenciesDto;
import com.example.demo.model.dto.ConvertCurrenciesResponseDto;
import com.example.demo.model.dto.ExchangeConversionsHistoryDto;
import com.example.demo.model.dto.ExchangeRateDto;
import com.example.demo.model.enums.ConversionHistoryStatus;
import com.example.demo.model.exceptions.BadRequestException;
import com.example.demo.model.exceptions.NotFoundException;
import com.example.demo.model.pojo.ExchangeRateConversions;
import com.example.demo.model.pojo.ExchangeRateHistory;
import com.example.demo.model.repository.ExchangeRateConversionsRepository;
import com.example.demo.model.repository.ExchangeRateHistoryRepository;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeConversionsService {
    private final ExchangeRateHistoryRepository exchangeRateHistoryRepository;
    private final ExchangeRateConversionsRepository exchangeRateConversionsRepository;
    private final ModelMapper mapper;

    public ExchangeConversionsService(ExchangeRateHistoryRepository exchangeRateHistoryRepository, ExchangeRateConversionsRepository exchangeRateConversionsRepository, ModelMapper mapper) {
        this.exchangeRateHistoryRepository = exchangeRateHistoryRepository;
        this.exchangeRateConversionsRepository = exchangeRateConversionsRepository;
        this.mapper = mapper;
    }

    public ConvertCurrenciesResponseDto convert(ConvertCurrenciesDto convertCurrenciesDto) {
        JSONObject jsonObject = sendHttpRequest(convertCurrenciesDto);
        try{
            if (jsonObject == null){
                throw new NotFoundException("Currency not found!");
            }
        }catch (org.json.JSONException e){
            throw new NotFoundException("Currency not found!");
        }

        ConvertCurrenciesResponseDto result = new ConvertCurrenciesResponseDto();
        ExchangeRateHistory erh = new ExchangeRateHistory();

        validateResponse(convertCurrenciesDto, jsonObject, erh);

        String firstCurrency = convertCurrenciesDto.getFirstCurrency();
        String secondCurrency = convertCurrenciesDto.getSecondCurrency();
        BigDecimal amountFirstCurrency = convertCurrenciesDto.getAmountFirstCurrency();

        BigDecimal amount = new BigDecimal(0);
        try{
            amount = jsonObject.getJSONObject("conversion_rates").getBigDecimal(convertCurrenciesDto.getSecondCurrency());
        }catch (org.json.JSONException e){
            throw new NotFoundException("Currency not found!");
        }

        result.setFirstCurrency(firstCurrency);
        result.setSecondCurrency(secondCurrency);
        result.setAmountFirstCurrency(amountFirstCurrency);
        result.setConvertedAmount(amountFirstCurrency.multiply(amount));
        result.setRate(amount.toString());
        result.setTimestamp(new Timestamp(System.currentTimeMillis()));
        result.setIdentifier(firstCurrency + "/" + secondCurrency);

        ExchangeRateConversions conversion = mapper.map(result, ExchangeRateConversions.class);
        conversion.setConversionStatus(ConversionHistoryStatus.VALID);
        exchangeRateConversionsRepository.save(conversion);

        return result;
    }

    private void validateResponse(ExchangeRateDto exchangeRateDto, JSONObject jsonObject, ExchangeRateHistory erh) {
        if(jsonObject.getString("result").equals("error")){
            mapper.map(exchangeRateDto, erh);
            erh.setTimestamp(new Timestamp(System.currentTimeMillis()));
            erh.setConversionStatus(ConversionHistoryStatus.INVALID);
            exchangeRateHistoryRepository.save(erh);
            throw new BadRequestException("Invalid Currency!");
        }
    }

    private JSONObject sendHttpRequest(ExchangeRateDto exchangeRateDto) {
        String url = "https://v6.exchangerate-api.com/v6/11b3a79edafc1cd3fe4caba5/latest/" + exchangeRateDto.getFirstCurrency();
        StringBuilder response = new StringBuilder();
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new JSONObject(response.toString());
    }

    public List<ExchangeConversionsHistoryDto> showExchangeHistory() {
        List<ExchangeRateConversions> exchangeRateHistoryList = exchangeRateConversionsRepository.findAll();
        return exchangeRateHistoryList.stream().map(c -> mapper.map(c, ExchangeConversionsHistoryDto.class)).collect(Collectors.toList());
    }
}

package com.example.demo.service;

import com.example.demo.model.dto.*;
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
public class ExchangeRateService {

    private final ExchangeRateHistoryRepository exchangeRateHistoryRepository;
    private final ExchangeRateConversionsRepository exchangeRateConversionsRepository;
    private final ModelMapper mapper;

    public ExchangeRateService(ExchangeRateHistoryRepository exchangeRateHistoryRepository, ExchangeRateConversionsRepository exchangeRateConversionsRepository, ModelMapper mapper){
        this.exchangeRateHistoryRepository = exchangeRateHistoryRepository;
        this.exchangeRateConversionsRepository = exchangeRateConversionsRepository;
        this.mapper = mapper;
    }

    public ExchangeRateResponseDTO getExchangeRate(ExchangeRateDto exchangeRateDto) {
        JSONObject jsonObject = sendHttpRequest(exchangeRateDto);
        try{
            if (jsonObject == null){
                throw new NotFoundException("Currency not found!");
            }
            BigDecimal rate = jsonObject.getJSONObject("conversion_rates").getBigDecimal(exchangeRateDto.getSecondCurrency());
            if (rate == null){
                throw new NotFoundException("No conversion rates!");
            }
        }catch (org.json.JSONException e){
            throw new NotFoundException("Currency not found!");
        }


        ExchangeRateResponseDTO result = new ExchangeRateResponseDTO();
        ExchangeRateHistory erh = mapper.map(exchangeRateDto, ExchangeRateHistory.class);

        validateResponse(exchangeRateDto, jsonObject, erh);

        erh.setTimestamp(new Timestamp(System.currentTimeMillis()));
        erh.setConversionStatus(ConversionHistoryStatus.VALID);
        exchangeRateHistoryRepository.save(erh);

        String firstCurrency = exchangeRateDto.getFirstCurrency();
        String secondCurrency = exchangeRateDto.getSecondCurrency();

        String rateResult = "";
        try{
            rateResult = jsonObject.getJSONObject("conversion_rates").getBigDecimal(secondCurrency).toString();
        }catch (org.json.JSONException e){
            throw new NotFoundException("Currency not found!");
        }
        result.setFirstCurrency(firstCurrency);
        result.setSecondCurrency(secondCurrency);
        result.setTimestamp(new Timestamp(System.currentTimeMillis()));
        result.setRate(rateResult);
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
}

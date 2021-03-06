package by.skopinau.cryptocurrencywatcher.controller;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dto.CurrencyResponse;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;
import by.skopinau.cryptocurrencywatcher.mapper.CurrencyMapper;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<CurrencyResponse>> getAvailableCurrencies() {
        ResponseEntity<List<CurrencyResponse>> response = ResponseEntity.noContent().build();
        List<CurrencyResponse> currenciesResponse = new ArrayList<>();

        try {
            currencyService.findAll().forEach((currency -> currenciesResponse.add(CurrencyMapper.INSTANCE.currencyEntityToCurrencyResponse(currency))));
            response = new ResponseEntity<>(currenciesResponse, HttpStatus.OK);
        } catch (CurrencyNotFoundException e) {
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/")
    public ResponseEntity<Double> getActualCurrencyPrice(@RequestParam String symbol) {
        ResponseEntity<Double> response = ResponseEntity.notFound().build();

        try {
            Currency currency = currencyService.findBySymbol(symbol);
            Double price = currency.getPriceUsd();
            response = new ResponseEntity<>(price, HttpStatus.OK);
        } catch (CurrencyNotFoundException e) {
            e.printStackTrace();
        }

        return response;
    }
}
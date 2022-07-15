package by.skopinau.cryptocurrencywatcher.service.impl;

import by.skopinau.cryptocurrencywatcher.config.AppConfig;
import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dal.repository.CurrencyRepository;
import by.skopinau.cryptocurrencywatcher.dto.CurrencyRequest;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;
import by.skopinau.cryptocurrencywatcher.mapper.CurrencyMapper;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository, RestTemplate restTemplate) {
        this.currencyRepository = currencyRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Currency> findAll() throws CurrencyNotFoundException {
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.size() != 0) {
            return currencies;
        } else {
            throw new CurrencyNotFoundException();
        }
    }

    @Override
    public Currency findBySymbol(String symbol) throws CurrencyNotFoundException {
        Currency currency = currencyRepository.findCurrencyBySymbol(symbol);
        if (Objects.nonNull(currency)) {
            return currency;
        } else {
            throw new CurrencyNotFoundException(symbol);
        }
    }

    @Override
    public double getPrice(long id) throws CurrencyNotFoundException {
        Currency currency = currencyRepository.findCurrencyById(id);
        if (Objects.nonNull(currency)) {
            return currency.getPriceUsd();
        } else {
            throw new CurrencyNotFoundException(id);
        }
    }

    // TODO transactional
    @Override
    public void updateAll() {
        List<Currency> currencyList = currencyRepository.findAll();
        for (Currency currency : currencyList) {
            Currency actualCurrency = getActualCurrency(currency.getId());
            currencyRepository.save(actualCurrency);
        }
    }

    // TODO custom class
    private Currency getActualCurrency(long id) {
        CurrencyRequest[] template = null;
        while (template == null) {
            template = restTemplate.getForObject(String.format(AppConfig.URL, id), CurrencyRequest[].class);
        }
        return CurrencyMapper.INSTANCE.currencyRequestToCurrencyEntity(template[0]);
    }
}

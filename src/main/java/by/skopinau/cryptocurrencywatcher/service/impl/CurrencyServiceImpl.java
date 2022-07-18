package by.skopinau.cryptocurrencywatcher.service.impl;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dal.repository.CurrencyRepository;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;
import by.skopinau.cryptocurrencywatcher.exception.message.CurrencyMessage;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import by.skopinau.cryptocurrencywatcher.service.util.CurrencyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyClient currencyClient;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyClient currencyClient) {
        this.currencyRepository = currencyRepository;
        this.currencyClient = currencyClient;
    }

    @Override
    public List<Currency> findAll() throws CurrencyNotFoundException {
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.size() != 0) {
            return currencies;
        } else {
            throw new CurrencyNotFoundException(CurrencyMessage.CURRENCY_NOT_FOUND.getMessage());
        }
    }

    @Override
    public Currency findBySymbol(String symbol) throws CurrencyNotFoundException {
        Currency currency = currencyRepository.findCurrencyBySymbol(symbol);
        if (Objects.nonNull(currency)) {
            return currency;
        } else {
            throw new CurrencyNotFoundException(String.format(CurrencyMessage.CURRENCY_NOT_FOUND_BY_SYMBOL.getMessage(), symbol));
        }
    }

    @Override
    public double getPrice(long id) throws CurrencyNotFoundException {
        Currency currency = currencyRepository.findCurrencyById(id);
        if (Objects.nonNull(currency)) {
            return currency.getPriceUsd();
        } else {
            throw new CurrencyNotFoundException(String.format(CurrencyMessage.CURRENCY_NOT_FOUND_BY_ID.getMessage(), id));
        }
    }

    // TODO transactional
    @Override
    public void updateAll() {
        List<Currency> currencyList = currencyRepository.findAll();
        for (Currency currency : currencyList) {
            Currency actualCurrency = currencyClient.getActualCurrency((currency.getId()));
            currencyRepository.save(actualCurrency);
        }
    }
}

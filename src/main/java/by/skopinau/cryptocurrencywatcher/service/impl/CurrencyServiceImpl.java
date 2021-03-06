package by.skopinau.cryptocurrencywatcher.service.impl;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dal.repository.CurrencyRepository;
import by.skopinau.cryptocurrencywatcher.exception.CoinLoreResponseException;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;
import by.skopinau.cryptocurrencywatcher.exception.message.CurrencyNotFoundMessage;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import by.skopinau.cryptocurrencywatcher.service.client.CurrencyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new CurrencyNotFoundException(CurrencyNotFoundMessage.CURRENCY_NOT_FOUND.getMessage());
        }
    }

    @Override
    public Currency findBySymbol(String symbol) throws CurrencyNotFoundException {
        Currency currency = currencyRepository.findCurrencyBySymbol(symbol);
        if (Objects.nonNull(currency)) {
            return currency;
        } else {
            throw new CurrencyNotFoundException(String.format(CurrencyNotFoundMessage.CURRENCY_NOT_FOUND_BY_SYMBOL.getMessage(), symbol));
        }
    }

    @Override
    public double getPrice(long id) throws CurrencyNotFoundException {
        Currency currency = currencyRepository.findCurrencyById(id);
        if (Objects.nonNull(currency)) {
            return currency.getPriceUsd();
        } else {
            throw new CurrencyNotFoundException(String.format(CurrencyNotFoundMessage.CURRENCY_NOT_FOUND_BY_ID.getMessage(), id));
        }
    }

    @Override
    @Transactional
    public void updateAll() {
        List<Currency> currencyList = currencyRepository.findAll();
        for (Currency currency : currencyList) {
            try {
                Currency actualCurrency = currencyClient.getActualCurrency((currency.getId()));
                currencyRepository.save(actualCurrency);
            } catch (CoinLoreResponseException e) {
                e.printStackTrace();
            }
        }
    }
}

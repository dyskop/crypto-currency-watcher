package by.skopinau.cryptocurrencywatcher.service;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dal.repository.CurrencyRepository;
import by.skopinau.cryptocurrencywatcher.exception.CoinLoreResponseException;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;
import by.skopinau.cryptocurrencywatcher.exception.message.CurrencyNotFoundMessage;
import by.skopinau.cryptocurrencywatcher.service.client.CurrencyClient;
import by.skopinau.cryptocurrencywatcher.service.impl.CurrencyServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTests {
    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyClient currencyClient;

    private final List<Currency> currencies = new ArrayList<>();

    @BeforeEach
    void init() {
        currencyService = new CurrencyServiceImpl(currencyRepository, currencyClient);

        Currency currency1 = new Currency(1L, "BTC", 0.0);
        Currency currency2 = new Currency(2L, "ETH", 1.1);
        Currency currency3 = new Currency(3L, "SOL", 2.2);

        currencies.add(currency1);
        currencies.add(currency2);
        currencies.add(currency3);
    }

    @Test
    void testFindAll_whenCurrenciesPresent_thenReturnCurrenciesList() throws CurrencyNotFoundException {
        // given
        List<Currency> expected = currencies;
        when(currencyRepository.findAll()).thenReturn(expected);

        // when
        List<Currency> actual = currencyService.findAll();

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindAll_whenCurrenciesNotPresent_thenCurrencyNotFoundExceptionThrows() {
        // given
        List<Currency> expected = new ArrayList<>();
        when(currencyRepository.findAll()).thenReturn(expected);

        // when
        // then
        Assertions.assertThrows(CurrencyNotFoundException.class,
                () -> currencyService.findAll(), CurrencyNotFoundMessage.CURRENCY_NOT_FOUND.getMessage());
    }

    @Test
    void testFindBySymbol_whenCurrencyFound_thenReturnCurrency() throws CurrencyNotFoundException {
        // given
        Currency currency2 = new Currency(2L, "ETH", 1.1);
        when(currencyRepository.findCurrencyBySymbol("ETH")).thenReturn(currency2);
        Currency expected = currencies.get(1);

        // when
        Currency actual = currencyService.findBySymbol("ETH");

        // then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindBySymbol_whenCurrencyNotFound_thenCurrencyNotFoundExceptionThrows() {
        // given
        String symbol = "AAA";
        when(currencyRepository.findCurrencyBySymbol(symbol)).thenReturn(null);

        // when
        // then
        Assertions.assertThrows(CurrencyNotFoundException.class,
                () -> currencyService.findBySymbol(symbol),
                String.format(CurrencyNotFoundMessage.CURRENCY_NOT_FOUND_BY_SYMBOL.getMessage(), symbol));
    }

    @Test
    void testGetPrice_whenCurrencyFound_thenReturnCurrencyPrice() throws CurrencyNotFoundException {
        // given
        Currency currency2 = new Currency(2L, "ETH", 1.1);
        when(currencyRepository.findCurrencyById(2L)).thenReturn(currency2);
        double expected = currencies.get(1).getPriceUsd();

        // when
        double actual = currencyService.getPrice(2L);

        // then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetPrice_whenCurrencyNotFound_thenCurrencyNotFoundExceptionThrows() {
        // given
        long id = 1L;
        when(currencyRepository.findCurrencyById(id)).thenReturn(null);

        // when
        // then
        Assertions.assertThrows(CurrencyNotFoundException.class,
                () -> currencyService.getPrice(id),
                String.format(CurrencyNotFoundMessage.CURRENCY_NOT_FOUND_BY_ID.getMessage(), id));
    }

    @Test
    void testUpdateAll_whenGivenCurrenciesList_thenPriceOfEachCurrencyUpdates() throws CoinLoreResponseException {
        // given
        when(currencyRepository.findAll()).thenReturn(currencies);

        Currency actualCurrency1 = new Currency(1L, "BTC", 10.0);
        Currency actualCurrency2 = new Currency(2L, "ETH", 11.1);
        Currency actualCurrency3 = new Currency(3L, "SOL", 12.2);

        when(currencyClient.getActualCurrency(1L)).thenReturn(actualCurrency1);
        when(currencyClient.getActualCurrency(2L)).thenReturn(actualCurrency2);
        when(currencyClient.getActualCurrency(3L)).thenReturn(actualCurrency3);

        // when
        currencyService.updateAll();

        // then
        verify(currencyRepository, times(1)).save(actualCurrency1);
        verify(currencyRepository, times(1)).save(actualCurrency2);
        verify(currencyRepository, times(1)).save(actualCurrency3);
    }
}
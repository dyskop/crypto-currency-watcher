package by.skopinau.cryptocurrencywatcher.service;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;

import java.util.List;

/**
 * Service for {@link Currency} entity.
 */
public interface CurrencyService {
    /**
     * Finds all {@link Currency} entities.
     *
     * @return {@link List<Currency>}
     * @throws CurrencyNotFoundException if
     *                                   there is no entity in the database
     */
    List<Currency> findAll() throws CurrencyNotFoundException;

    /**
     * Finds {@link Currency} by symbol.
     *
     * @param symbol is used to define entity to find
     * @return {@link Currency}
     * @throws CurrencyNotFoundException if
     * there is no {@link Currency} entity with the
     * given symbol in the database
     */
    Currency findBySymbol(String symbol) throws CurrencyNotFoundException;

    /**
     * Gets the price value for the {@link Currency}
     * entity from the database.
     *
     * @param id is used to define entity to find
     * @return price value for the {@link Currency} entity
     * @throws CurrencyNotFoundException if
     * there is no {@link Currency} entity with the
     * given id in the database
     */
    double getPrice(long id) throws CurrencyNotFoundException;

    /**
     * Updates the price value of all available {@link Currency}
     * entities to those provided by the
     * {@link <a href="https://www.coinlore.com/cryptocurrency-data-api#3">open CoinLore API</a>}.
     */
    void updateAll();
}

package by.skopinau.cryptocurrencywatcher.service.client;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.exception.CoinLoreResponseException;

/**
 * Client for {@link by.skopinau.cryptocurrencywatcher.service.CurrencyService}.
 */
public interface CurrencyClient {
    /**
     * Gets the actual {@link Currency} from the open API.
     *
     * @param id is used to define currency to find
     * @return {@link Currency}
     * @throws CoinLoreResponseException if the open API
     * is not responding
     */
    Currency getActualCurrency(long id) throws CoinLoreResponseException;
}

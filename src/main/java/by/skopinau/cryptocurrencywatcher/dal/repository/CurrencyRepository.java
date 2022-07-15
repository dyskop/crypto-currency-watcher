package by.skopinau.cryptocurrencywatcher.dal.repository;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Currency} entity.
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    /**
     * Finds {@link Currency} by id.
     * @param id is used to define entity to find
     * @return {@link Currency}
     */
    Currency findCurrencyById(long id);

    /**
     * Finds {@link Currency} by symbol.
     * @param symbol is used to define entity to find
     * @return {@link Currency}
     */
    Currency findCurrencyBySymbol(String symbol);
}

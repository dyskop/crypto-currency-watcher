package by.skopinau.cryptocurrencywatcher.service;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Service for background database update
 * and logging of price changes by more than
 * 1 percent.
 */
public interface SchedulerService {
    /**
     * Checks the percentage change in the currency price for each {@link by.skopinau.cryptocurrencywatcher.dal.entity.User},
     * having previously updated the currencies, and if it is greater
     * than one, then displays a message in the log.
     */
    @Scheduled
    void checkCurrencies();
}

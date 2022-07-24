package by.skopinau.cryptocurrencywatcher.service.util;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
public class CurrencyChecker {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyChecker(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public void checkUserCurrency(User user) {
        String symbol = user.getSymbol();

        try {
            Currency currency = currencyService.findBySymbol(symbol);

            double userPrice = user.getPrice();
            double actualPrice = currency.getPriceUsd();
            double percent = getPercentChange(userPrice, actualPrice);

            if (percent < -1 || percent > 1) {
                log.warn(String.format(Locale.US,
                        "%s %s %.2f", symbol, user.getUsername(), percent));
            }
        } catch (CurrencyNotFoundException e) {
            e.printStackTrace();
        }
    }

    private double getPercentChange(double userPrice, double actualPrice) {
        return ((actualPrice - userPrice) / userPrice) * 100;
    }
}

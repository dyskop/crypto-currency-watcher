package by.skopinau.cryptocurrencywatcher.service.impl;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import by.skopinau.cryptocurrencywatcher.service.SchedulerService;
import by.skopinau.cryptocurrencywatcher.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {
    private static final long TIME_INTERVAL = 60000;

    private final CurrencyService currencyService;
    private final UserService userService;

    @Autowired
    public SchedulerServiceImpl(CurrencyService currencyService, UserService userService) {
        this.currencyService = currencyService;
        this.userService = userService;
    }

    @Scheduled (fixedRate = TIME_INTERVAL)
    public void checkCurrencies() {
        currencyService.updateAll();

        List<User> userList = userService.findAll();
        for (User user : userList) {
            String symbol = user.getSymbol();
            Currency currency = currencyService.findBySymbol(symbol);

            double userPrice = user.getPrice();
            double actualPrice = currency.getPriceUsd();
            double percent = getPercentChange(userPrice, actualPrice);

            if (percent < -1 || percent > 1) {
                log.warn(String.format(Locale.US,
                        "%s %s %.2f", symbol, user.getUsername(), percent));
            }
        }
    }

    private static double getPercentChange(double userPrice, double actualPrice) {
        return ((actualPrice - userPrice) / userPrice) * 100;
    }
}
package by.skopinau.cryptocurrencywatcher.service.impl;

import by.skopinau.cryptocurrencywatcher.config.AppConfig;
import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import by.skopinau.cryptocurrencywatcher.service.SchedulerService;
import by.skopinau.cryptocurrencywatcher.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@EnableScheduling
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final CurrencyService currencyService;
    private final UserService userService;

    @Autowired
    public SchedulerServiceImpl(CurrencyService currencyService, UserService userService) {
        this.currencyService = currencyService;
        this.userService = userService;
    }

    @Scheduled(fixedRate = AppConfig.TIME_INTERVAL)
    public void checkCurrencies() {
        currencyService.updateAll();
        List<User> userList = userService.findAll();
        userList.parallelStream().forEach(this::checkUserCurrency);
    }

    private void checkUserCurrency(User user) {
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

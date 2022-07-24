package by.skopinau.cryptocurrencywatcher.service.impl;

import by.skopinau.cryptocurrencywatcher.config.AppConfig;
import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import by.skopinau.cryptocurrencywatcher.service.SchedulerService;
import by.skopinau.cryptocurrencywatcher.service.UserService;
import by.skopinau.cryptocurrencywatcher.service.util.CurrencyChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@EnableScheduling
@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final CurrencyService currencyService;
    private final UserService userService;
    private final CurrencyChecker currencyChecker;

    @Autowired
    public SchedulerServiceImpl(CurrencyService currencyService, UserService userService, CurrencyChecker currencyChecker) {
        this.currencyService = currencyService;
        this.userService = userService;
        this.currencyChecker = currencyChecker;
    }

    @Override
    @Scheduled(fixedRate = AppConfig.TIME_INTERVAL)
    public void checkCurrencies() {
        currencyService.updateAll();
        List<User> userList = userService.findAll();
        userList.parallelStream().forEach(currencyChecker::checkUserCurrency);
    }
}

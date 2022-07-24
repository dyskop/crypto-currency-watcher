package by.skopinau.cryptocurrencywatcher.service.util;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CurrencyCheckerTests {
    private static MemoryAppender memoryAppender;
    private static final String LOGGER_NAME = "by.skopinau.cryptocurrencywatcher.service.util";
    private static final String MSG = "%s %s %.2f";

    private CurrencyChecker currencyChecker;

    @Mock
    private CurrencyService currencyService;

    @BeforeEach
    public void init() {
        currencyChecker = new CurrencyChecker(currencyService);

        Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.WARN);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @AfterAll
    static void cleanUp() {
        memoryAppender.reset();
        memoryAppender.stop();
    }

    @Test
    void testCheckUserCurrency_whenCurrencyPriceChangedMoreThenOnePercent_thenLogWarn() throws CurrencyNotFoundException {
        // given
        Currency currency = new Currency(1L, "BTC", 2.0);
        User user = new User("user", "BTC", 1.0);

        when(currencyService.findBySymbol("BTC")).thenReturn(currency);

        String message = String.format(Locale.US, MSG, currency.getSymbol(), user.getUsername(), 100.00);

        // when
        currencyChecker.checkUserCurrency(user);

        // then
        assertThat(memoryAppender.countEventsForLogger(LOGGER_NAME)).isEqualTo(1);
        assertThat(memoryAppender.contains(message, Level.WARN)).isTrue();
    }

    @Test
    void testCheckUserCurrency_whenCurrencyPriceChangedLessThenOnePercent_thenDoNotLogWarn() throws CurrencyNotFoundException {
        // given
        Currency currency = new Currency(1L, "BTC", 100.5);
        User user = new User("user", "BTC", 100.0);

        when(currencyService.findBySymbol("BTC")).thenReturn(currency);

        // when
        currencyChecker.checkUserCurrency(user);

        // then
        assertThat(memoryAppender.countEventsForLogger(LOGGER_NAME)).isEqualTo(0);
    }

    @Test
    void testCheckUserCurrency_whenCurrencyPriceChangedByOnePercent_thenDoNotLogWarn() throws CurrencyNotFoundException {
        // given
        Currency currency = new Currency(1L, "BTC", 101.0);
        User user = new User("user", "BTC", 100.0);

        when(currencyService.findBySymbol("BTC")).thenReturn(currency);

        // when
        currencyChecker.checkUserCurrency(user);

        // then
        assertThat(memoryAppender.countEventsForLogger(LOGGER_NAME)).isEqualTo(0);
    }
}
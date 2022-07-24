package by.skopinau.cryptocurrencywatcher.service;

import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import by.skopinau.cryptocurrencywatcher.service.impl.SchedulerServiceImpl;
import by.skopinau.cryptocurrencywatcher.service.util.CurrencyChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTests {
    private SchedulerService schedulerService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private UserService userService;

    @Mock
    private CurrencyChecker currencyChecker;

    @BeforeEach
    void init() {
        schedulerService = new SchedulerServiceImpl(currencyService, userService, currencyChecker);
    }

    @Test
    void testCheckCurrencies_whenGivenUsers_thenUpdateAllCurAndInvokesCheckUserCurForEachUser() {
        // given
        List<User> users = new ArrayList<>();

        User user1 = new User(UUID.randomUUID(), "user1", "BTC", 100.0);
        User user2 = new User(UUID.randomUUID(), "user2", "ETH", 101.0);
        User user3 = new User(UUID.randomUUID(), "user3", "SOL", 102.0);

        users.add(user1);
        users.add(user2);
        users.add(user3);

        when(userService.findAll()).thenReturn(users);

        // when
        schedulerService.checkCurrencies();

        // then
        verify(currencyService, times(1)).updateAll();
        verify(currencyChecker, times(1)).checkUserCurrency(user1);
        verify(currencyChecker, times(1)).checkUserCurrency(user2);
        verify(currencyChecker, times(1)).checkUserCurrency(user3);
    }
}
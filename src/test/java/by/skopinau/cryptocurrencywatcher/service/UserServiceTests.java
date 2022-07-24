package by.skopinau.cryptocurrencywatcher.service;

import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import by.skopinau.cryptocurrencywatcher.dal.repository.UserRepository;
import by.skopinau.cryptocurrencywatcher.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testFindAll_whenUsersPresent_thenReturnUsersList() {
        // given
        List<User> expected = new ArrayList<>();

        User user1 = new User("user1", "BTC", 0.0);
        User user2 = new User("user2", "ETH", 1.1);
        User user3 = new User("user3", "SOL", 2.2);

        expected.add(user1);
        expected.add(user2);
        expected.add(user3);

        when(userRepository.findAll()).thenReturn(expected);

        // when
        List<User> actual = userService.findAll();

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindAll_whenUsersNotPresent_thenReturnEmptyList() {
        // given
        List<User> expected = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(expected);

        // when
        List<User> actual = userService.findAll();

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSave_whenSaveUserThreeTimes_ThenSaveMethodInvokesThreeTimes() {
        // given
        User user1 = new User("user1", "BTC", 0.0);
        User user2 = new User("user2", "ETH", 1.1);
        User user3 = new User("user3", "SOL", 2.2);
        // when
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        // then
        verify(userRepository, times(3)).save(any());
    }
}
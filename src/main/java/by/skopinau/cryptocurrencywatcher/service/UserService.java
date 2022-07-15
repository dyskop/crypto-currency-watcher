package by.skopinau.cryptocurrencywatcher.service;

import by.skopinau.cryptocurrencywatcher.dal.entity.User;

import java.util.List;

/**
 * Service for {@link User} entity.
 */
public interface UserService {
    /**
     * Finds all {@link User} entity.
     *
     * @return {@link List<User>}
     */
    List<User> findAll();

    /**
     * Saves {@link User} entity to the database.
     *
     * @param user is used to define {@link User} entity
     * @return saved {@link User} entity
     */
    User save(User user);
}

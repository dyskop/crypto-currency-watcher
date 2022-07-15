package by.skopinau.cryptocurrencywatcher.dal.repository;

import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link User} entity.
 */
public interface UserRepository extends JpaRepository<User, UUID> {
}

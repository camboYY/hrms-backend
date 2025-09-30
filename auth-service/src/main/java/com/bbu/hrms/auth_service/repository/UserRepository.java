package com.bbu.hrms.auth_service.repository;

import com.bbu.hrms.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to lookup
     * @return an {@link Optional} containing the user, or empty if no user exists with the given username
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address to lookup
     * @return an {@link Optional} containing the user, or empty if no user exists with the given email address
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the username to check for existence
     * @return true if a user exists with the given username, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists with the given email address.
     *
     * @param email the email address to check for existence
     * @return true if a user exists with the given email address, false otherwise
     */
    boolean existsByEmail(String email);

    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);
}

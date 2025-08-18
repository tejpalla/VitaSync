package com.vitasync.repository;

import java.util.Optional;
import com.vitasync.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email address.
     *
     * @param email the email address of the user
     * @return Optional containing the user with the specified email, or empty Optional if no user found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by their email address.
     *
     * @param email the email address to check
     * @return true if a user with the specified email exists, false otherwise
     */
    Optional<User> findByPhone(String phone);
    
}

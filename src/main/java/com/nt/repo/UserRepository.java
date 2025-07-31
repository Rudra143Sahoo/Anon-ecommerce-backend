package com.nt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nt.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobile(String mobile);

    // Add these two lines:
    boolean existsByMobile(String mobile);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email); // ✅ Added this line

    Optional<User> findByMobileAndPassword(String mobile, String password);
}

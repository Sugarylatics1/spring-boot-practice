package com.sugarylatics.springbootprac.repository;

import com.sugarylatics.springbootprac.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UUID>{
    Optional<User> findByApiKey(String apiKey);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

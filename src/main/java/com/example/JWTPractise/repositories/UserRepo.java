package com.example.JWTPractise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.JWTPractise.models.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

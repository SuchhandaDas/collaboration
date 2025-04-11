package com.test.collaboration.repositories;

import com.test.collaboration.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByTokenAndRevokedFalse(String token);
}

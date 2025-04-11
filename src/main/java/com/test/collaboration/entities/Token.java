package com.test.collaboration.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private String token;
    private Long employeeId;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private boolean revoked;
}

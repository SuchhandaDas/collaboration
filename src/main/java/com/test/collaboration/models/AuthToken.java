package com.test.collaboration.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    private String token;
    private long employeeId;
    private LocalDateTime expiry;

    public boolean isExpired() {
        return expiry.isBefore(LocalDateTime.now());
    }
}

package com.test.collaboration.services;

import com.test.collaboration.entities.Employee;
import com.test.collaboration.entities.Token;
import com.test.collaboration.exceptions.InvalidLoginCredentials;
import com.test.collaboration.models.EmployeeDTO;
import com.test.collaboration.repositories.EmployeeRepository;
import com.test.collaboration.repositories.TokenRepository;
import com.test.collaboration.services.impl.AuthServiceImpl;
import com.test.collaboration.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    private TokenRepository tokenRepository;
    private EmployeeRepository employeeRepository;
    private JWTUtil jwtUtil;

    private AuthServiceImpl authService;

    @BeforeEach
    void setup() {
        tokenRepository = mock(TokenRepository.class);
        employeeRepository = mock(EmployeeRepository.class);
        jwtUtil = mock(JWTUtil.class);

        authService = new AuthServiceImpl(tokenRepository, employeeRepository, jwtUtil);
    }

    @Test
    void login_validCredentials_returnsJwtAndSavesEmployeeAndToken() {
        EmployeeDTO dto = new EmployeeDTO(12345L, "Alice");
        String jwt = "jwt-token";

        when(employeeRepository.existsById(12345L)).thenReturn(false);
        when(jwtUtil.generateToken(12345L)).thenReturn(jwt);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getIssuedAt()).thenReturn(new Date());
        when(mockClaims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 3600000));
        when(jwtUtil.getClaimsFromToken(jwt)).thenReturn(mockClaims);

        String result = authService.login(dto);

        assertEquals(jwt, result);
        verify(employeeRepository).save(any(Employee.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void login_invalidCredentials_throwsException() {
        EmployeeDTO dto = new EmployeeDTO(null, null); // invalid data

        assertThrows(InvalidLoginCredentials.class, () -> authService.login(dto));
        verifyNoInteractions(employeeRepository, tokenRepository);
    }

    @Test
    void logout_existingToken_setsRevokedTrue() {
        String tokenStr = "valid-token";

        Token token = Token.builder()
                .token(tokenStr)
                .revoked(false)
                .build();

        when(tokenRepository.findById(tokenStr)).thenReturn(Optional.of(token));

        authService.logout(tokenStr);

        assertTrue(token.isRevoked());
        verify(tokenRepository).save(token);
    }

    @Test
    void logout_nonExistingToken_doesNothing() {
        when(tokenRepository.findById("non-existent-token")).thenReturn(Optional.empty());

        authService.logout("non-existent-token");

        verify(tokenRepository, never()).save(any());
    }
}

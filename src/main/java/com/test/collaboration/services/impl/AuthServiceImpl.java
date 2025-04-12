package com.test.collaboration.services.impl;

import com.test.collaboration.entities.Employee;
import com.test.collaboration.entities.Token;
import com.test.collaboration.exceptions.InvalidLoginCredentials;
import com.test.collaboration.models.EmployeeDTO;
import com.test.collaboration.repositories.EmployeeRepository;
import com.test.collaboration.repositories.TokenRepository;
import com.test.collaboration.services.AuthService;
import com.test.collaboration.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import static com.test.collaboration.validators.EmployeeValidator.isValid;

@Service
public class AuthServiceImpl implements AuthService {

    private final TokenRepository tokenRepository;
    private final EmployeeRepository employeeRepository;
    private final JWTUtil jwtUtil;

    public AuthServiceImpl(TokenRepository tokenRepository, EmployeeRepository employeeRepository, JWTUtil jwtUtil) {
        this.tokenRepository = tokenRepository;
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String login(EmployeeDTO employeeDTO) {
        // validate employee login request
        if(!isValid(employeeDTO)) throw new InvalidLoginCredentials("Invalid credentials");

        if (!employeeRepository.existsById(employeeDTO.getId())) {
            Employee employee = Employee.builder()
                    .id(employeeDTO.getId())
                    .name(employeeDTO.getName())
                    .build();
            employeeRepository.save(employee);
        }

        String jwtToken = jwtUtil.generateToken(employeeDTO.getId());
        Claims claim = jwtUtil.getClaimsFromToken(jwtToken);

        Token token = Token.builder()
                .employeeId(employeeDTO.getId())
                .token(jwtToken)
                .issuedAt(claim.getIssuedAt().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                .expiresAt(claim.getExpiration().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                .revoked(false)
                .build();
        tokenRepository.save(token);

        return jwtToken;
    }

    @Override
    public void logout(String token) {
        tokenRepository.findById(token).ifPresent(t-> {
            t.setRevoked(true);
            tokenRepository.save(t);
        });
    }
}

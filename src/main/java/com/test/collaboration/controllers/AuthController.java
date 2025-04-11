package com.test.collaboration.controllers;

import com.test.collaboration.entities.Employee;
import com.test.collaboration.entities.Token;
import com.test.collaboration.models.EmployeeDTO;
import com.test.collaboration.repositories.EmployeeRepository;
import com.test.collaboration.repositories.TokenRepository;
import com.test.collaboration.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final TokenRepository tokenRepository;
    private final EmployeeRepository employeeRepository;
    private final JWTUtil jwtUtil;

    public AuthController(TokenRepository tokenRepository, EmployeeRepository employeeRepository, JWTUtil jwtUtil) {
        this.tokenRepository = tokenRepository;
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody EmployeeDTO employeeDTO) {
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
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenRepository.findById(token).ifPresent(t-> {
                t.setRevoked(true);
                tokenRepository.save(t);
            });
        }
    }
}

package com.test.collaboration.services;

import com.test.collaboration.models.EmployeeDTO;

public interface AuthService {
    String login(EmployeeDTO employeeDTO);
    void logout(String token);
}

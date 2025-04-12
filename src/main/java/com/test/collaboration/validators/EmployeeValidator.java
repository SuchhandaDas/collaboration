package com.test.collaboration.validators;

import com.test.collaboration.models.EmployeeDTO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class EmployeeValidator {
    public static boolean isValid(EmployeeDTO employeeDTO) {
        if (Objects.nonNull(employeeDTO)
                && Objects.nonNull(employeeDTO.getId())
                && !StringUtils.isEmpty(employeeDTO.getName())) {
            return true;
        }
        return false;
    }
}

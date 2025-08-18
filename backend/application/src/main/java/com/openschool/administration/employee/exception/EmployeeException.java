package com.openschool.administration.employee.exception;

import com.openschool.common.exception.CustomerException;

public class EmployeeException extends CustomerException {
    public EmployeeException(String message) {
        super(message);
    }
}

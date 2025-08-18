package com.openschool.infrastructure.adapter.in.rest.common;

import com.openschool.common.exception.CustomerException;
import com.openschool.common.exception.ExcelProcessingException;
import com.openschool.common.exception.ExcelValidationException;
import com.openschool.common.exception.InvalidExcelFormatException;
import com.openschool.department.exception.DepartmentException;
import com.openschool.common.exception.DataNotFound;
import com.openschool.identity.exception.InvalidCredentialsException;
import com.openschool.identity.exception.UserAlreadyExistsException;
import com.openschool.common.exception.ForbiddenSetup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleRootUserExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleRootUserExists(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<String> handleRootUserExists(DataNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<String> handleDepartmentException(DepartmentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<String> handleCustomerException(CustomerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenSetup.class)
    public ResponseEntity<String> handleForbiddenSetup(ForbiddenSetup ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(ExcelProcessingException.class)
    public ResponseEntity<String> handleExcelProcessingException(ExcelProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidExcelFormatException.class)
    public ResponseEntity<String> handleInvalidExcelFormatException(InvalidExcelFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ExcelValidationException.class)
    public ResponseEntity<ExcelValidationErrorResponse> handleExcelValidationException(ExcelValidationException ex) {
        ExcelValidationErrorResponse response = new ExcelValidationErrorResponse(
            ex.getMessage(),
            ex.getValidationErrors()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Response class for Excel validation errors
    public static class ExcelValidationErrorResponse {
        private String message;
        private java.util.List<String> validationErrors;

        public ExcelValidationErrorResponse(String message, java.util.List<String> validationErrors) {
            this.message = message;
            this.validationErrors = validationErrors;
        }

        public String getMessage() { return message; }
        public java.util.List<String> getValidationErrors() { return validationErrors; }
    }
}

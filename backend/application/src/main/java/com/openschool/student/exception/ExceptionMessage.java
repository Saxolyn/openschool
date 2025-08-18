package com.openschool.student.exception;

public enum ExceptionMessage {
    STUDENT_NOT_FOUND("Student not found"),
    STUDENT_CODE_ALREADY_EXISTS("Student code already exists"),
    STUDENT_EMAIL_ALREADY_EXISTS("Student email already exists"),
    STUDENT_ALREADY_ENROLLED("Student is already enrolled in a class"),
    STUDENT_NOT_ACTIVE("Student is not active"),
    STUDENT_CANNOT_ENROLL("Student cannot be enrolled in class"),
    GUARDIAN_NOT_FOUND("Guardian not found"),
    GUARDIAN_EMAIL_ALREADY_EXISTS("Guardian email already exists"),
    GUARDIAN_PHONE_ALREADY_EXISTS("Guardian phone number already exists"),
    GUARDIAN_ALREADY_LINKED("Guardian is already linked to this student"),
    GUARDIAN_NOT_LINKED("Guardian is not linked to this student"),
    INVALID_STUDENT_DATA("Invalid student data provided"),
    INVALID_GUARDIAN_DATA("Invalid guardian data provided"),
    STUDENT_HAS_ACTIVE_ENROLLMENT("Student has active class enrollment"),
    AGE_NOT_SUITABLE_FOR_GRADE("Student age is not suitable for the selected grade");
    
    private final String message;
    
    ExceptionMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}

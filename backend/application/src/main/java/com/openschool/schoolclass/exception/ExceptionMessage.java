package com.openschool.schoolclass.exception;

public enum ExceptionMessage {
    CLASS_NOT_FOUND("Class not found"),
    CLASS_CODE_ALREADY_EXISTS("Class code already exists"),
    CLASS_NAME_ALREADY_EXISTS("Class name already exists for this grade and academic year"),
    CLASS_IS_FULL("Class has reached maximum capacity"),
    CLASS_NOT_ACTIVE("Class is not active"),
    CLASS_CANNOT_ENROLL_STUDENT("Class cannot enroll students"),
    CLASS_HAS_STUDENTS("Class still has enrolled students"),
    TEACHER_NOT_FOUND("Teacher not found"),
    TEACHER_ALREADY_ASSIGNED("Teacher is already assigned to this class"),
    TEACHER_NOT_AVAILABLE("Teacher is not available for assignment"),
    INVALID_CLASS_DATA("Invalid class data provided"),
    GRADE_NOT_FOUND("Grade not found for class"),
    ACADEMIC_YEAR_NOT_FOUND("Academic year not found for class"),
    STUDENT_NOT_IN_CLASS("Student is not enrolled in this class"),
    STUDENT_ALREADY_IN_CLASS("Student is already enrolled in this class"),
    CANNOT_DELETE_ACTIVE_CLASS("Cannot delete an active class with students");
    
    private final String message;
    
    ExceptionMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}

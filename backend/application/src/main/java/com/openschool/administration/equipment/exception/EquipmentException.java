package com.openschool.administration.equipment.exception;

import com.openschool.common.exception.ExceptionMessage;

public class EquipmentException extends RuntimeException {
    
    public EquipmentException(ExceptionMessage message) {
        super(message.getMessage());
    }
    
    public EquipmentException(String message) {
        super(message);
    }
    
    public EquipmentException(String message, Throwable cause) {
        super(message, cause);
    }
}

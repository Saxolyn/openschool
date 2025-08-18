package com.openschool.common.excel.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Pure Java utility for converting Excel cell values to Java types
 */
public class ExcelDataConverter {
    
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };
    
    public static Object convertValue(Object value, String targetType) {
        if (value == null) {
            return null;
        }
        
        String stringValue = value.toString().trim();
        
        if (stringValue.isEmpty()) {
            return null;
        }
        
        switch (targetType.toLowerCase()) {
            case "string":
                return stringValue;
                
            case "integer":
            case "int":
                try {
                    return Integer.parseInt(stringValue);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Cannot convert '" + stringValue + "' to Integer");
                }
                
            case "long":
                try {
                    return Long.parseLong(stringValue);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Cannot convert '" + stringValue + "' to Long");
                }
                
            case "double":
            case "decimal":
                try {
                    return Double.parseDouble(stringValue);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Cannot convert '" + stringValue + "' to Double");
                }
                
            case "boolean":
                String lowerValue = stringValue.toLowerCase();
                if (lowerValue.equals("true") || lowerValue.equals("yes") || lowerValue.equals("1")) {
                    return true;
                } else if (lowerValue.equals("false") || lowerValue.equals("no") || lowerValue.equals("0")) {
                    return false;
                } else {
                    throw new IllegalArgumentException("Cannot convert '" + stringValue + "' to Boolean");
                }
                
            case "date":
            case "localdate":
                for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                    try {
                        return LocalDate.parse(stringValue, formatter);
                    } catch (DateTimeParseException ignored) {
                        // Try next formatter
                    }
                }
                throw new IllegalArgumentException("Cannot convert '" + stringValue + "' to LocalDate");
                
            case "datetime":
            case "localdatetime":
                // Add datetime conversion if needed
                throw new IllegalArgumentException("LocalDateTime conversion not implemented yet");
                
            default:
                // For enum or custom types, return as string for business logic to handle
                return stringValue;
        }
    }
    
    public static String formatValue(Object value) {
        if (value == null) {
            return "";
        }
        
        if (value instanceof LocalDate) {
            return ((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "Yes" : "No";
        }
        
        return value.toString();
    }
}

package com.openschool.common.excel.validation;

import com.openschool.domain.common.excel.ExcelColumn;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Pure Java validation logic for Excel data
 */
public class ExcelDataValidator {
    
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };
    
    public static List<String> validateCellValue(Object value, ExcelColumn column, int rowNumber) {
        List<String> errors = new ArrayList<>();
        
        // Check required fields
        if (column.isRequired() && (value == null || value.toString().trim().isEmpty())) {
            errors.add(String.format("Row %d: %s is required", rowNumber, column.getName()));
            return errors;
        }
        
        // Skip validation for empty optional fields
        if (value == null || value.toString().trim().isEmpty()) {
            return errors;
        }
        
        String stringValue = value.toString().trim();
        
        // Type validation
        try {
            validateDataType(stringValue, column.getDataType(), rowNumber, column.getName(), errors);
        } catch (Exception e) {
            errors.add(String.format("Row %d: Invalid %s format for %s", rowNumber, 
                column.getDataType(), column.getName()));
        }
        
        // Pattern validation
        if (column.getValidationPattern() != null && !column.getValidationPattern().isEmpty()) {
            if (!Pattern.matches(column.getValidationPattern(), stringValue)) {
                errors.add(String.format("Row %d: %s does not match required pattern", 
                    rowNumber, column.getName()));
            }
        }
        
        return errors;
    }
    
    private static void validateDataType(String value, String dataType, int rowNumber, String columnName, List<String> errors) {
        switch (dataType.toLowerCase()) {
            case "string":
                // String is always valid
                break;
                
            case "integer":
            case "int":
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    errors.add(String.format("Row %d: %s must be a valid integer", rowNumber, columnName));
                }
                break;
                
            case "long":
                try {
                    Long.parseLong(value);
                } catch (NumberFormatException e) {
                    errors.add(String.format("Row %d: %s must be a valid number", rowNumber, columnName));
                }
                break;
                
            case "double":
            case "decimal":
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    errors.add(String.format("Row %d: %s must be a valid decimal number", rowNumber, columnName));
                }
                break;
                
            case "boolean":
                String lowerValue = value.toLowerCase();
                if (!lowerValue.equals("true") && !lowerValue.equals("false") && 
                    !lowerValue.equals("yes") && !lowerValue.equals("no") &&
                    !lowerValue.equals("1") && !lowerValue.equals("0")) {
                    errors.add(String.format("Row %d: %s must be true/false, yes/no, or 1/0", rowNumber, columnName));
                }
                break;
                
            case "date":
            case "localdate":
                boolean validDate = false;
                for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                    try {
                        LocalDate.parse(value, formatter);
                        validDate = true;
                        break;
                    } catch (DateTimeParseException ignored) {
                        // Try next formatter
                    }
                }
                if (!validDate) {
                    errors.add(String.format("Row %d: %s must be a valid date (yyyy-MM-dd, dd/MM/yyyy, etc.)", 
                        rowNumber, columnName));
                }
                break;
                
            default:
                // For enum or custom types, validation should be done in business logic
                break;
        }
    }
    
    public static void validateRequiredColumns(List<String> actualColumns, List<ExcelColumn> requiredColumns) {
        List<String> missingColumns = new ArrayList<>();
        
        for (ExcelColumn column : requiredColumns) {
            if (column.isRequired() && !actualColumns.contains(column.getName())) {
                missingColumns.add(column.getName());
            }
        }
        
        if (!missingColumns.isEmpty()) {
            throw new IllegalArgumentException(
                "Missing required columns: " + String.join(", ", missingColumns)
            );
        }
    }
}

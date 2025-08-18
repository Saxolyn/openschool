package com.openschool.domain.common.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Domain value object representing an Excel column definition
 */
@Getter
@Builder
@AllArgsConstructor
public class ExcelColumn {
    
    private final String name;
    private final String fieldName;
    private final String dataType;
    private final boolean required;
    private final String defaultValue;
    private final String validationPattern;
    private final String description;
    
    public ExcelColumn(String name, String fieldName, String dataType) {
        this.name = name;
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.required = false;
        this.defaultValue = null;
        this.validationPattern = null;
        this.description = null;
    }
    
    public ExcelColumn(String name, String fieldName, String dataType, boolean required) {
        this.name = name;
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.required = required;
        this.defaultValue = null;
        this.validationPattern = null;
        this.description = null;
    }
}

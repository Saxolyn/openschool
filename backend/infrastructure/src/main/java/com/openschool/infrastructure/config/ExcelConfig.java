package com.openschool.infrastructure.config;

import com.openschool.common.excel.port.ExcelReaderPort;
import com.openschool.common.excel.port.ExcelWriterPort;
import com.openschool.infrastructure.adapter.out.excel.ExcelReaderAdapter;
import com.openschool.infrastructure.adapter.out.excel.ExcelWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Excel processing components
 */
@Configuration
public class ExcelConfig {

    @Bean
    public ExcelReaderPort excelReaderPort() {
        return new ExcelReaderAdapter();
    }

    @Bean
    public ExcelWriterPort excelWriterPort() {
        return new ExcelWriterAdapter();
    }
}

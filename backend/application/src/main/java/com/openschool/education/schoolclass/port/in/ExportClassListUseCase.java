package com.openschool.education.schoolclass.port.in;

import com.openschool.common.excel.result.ExportResult;

public interface ExportClassListUseCase {

    /**
     * Export class list to Excel file
     */
    ExportResult exportClassList(String fileName);
}

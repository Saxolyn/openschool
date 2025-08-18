package com.openschool.education.student.port.in;

import com.openschool.common.excel.result.ExportResult;

public interface ExportStudentListUseCase {

    /**
     * Export student list to Excel file
     */
    ExportResult exportStudentList(String fileName);
}

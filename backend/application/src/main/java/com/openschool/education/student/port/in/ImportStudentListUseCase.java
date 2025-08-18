package com.openschool.education.student.port.in;

import com.openschool.common.excel.command.ImportExcelCommand;
import com.openschool.common.excel.result.ImportResult;
import com.openschool.domain.student.Student;

public interface ImportStudentListUseCase {

    /**
     * Import student list from Excel file
     */
    ImportResult<Student> importStudentList(ImportExcelCommand command);
}

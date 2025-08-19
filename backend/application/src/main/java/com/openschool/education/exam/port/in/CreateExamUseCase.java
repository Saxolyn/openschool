package com.openschool.education.exam.port.in;

import com.openschool.education.exam.port.in.command.CreateExamCommand;
import com.openschool.domain.exam.Exam;

public interface CreateExamUseCase {
    Exam createExam(CreateExamCommand command);
}


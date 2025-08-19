package com.openschool.education.mark.port.in;

import com.openschool.education.mark.port.in.command.CreateMarkCommand;
import com.openschool.domain.mark.Mark;

public interface CreateMarkUseCase {
    Mark createMark(CreateMarkCommand command);
}


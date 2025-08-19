package com.openschool.administration.library.port.in;

import com.openschool.administration.library.port.in.command.RegisterLibraryUserCommand;
import com.openschool.domain.library.LibraryUser;

public interface RegisterLibraryUserUseCase {
    LibraryUser registerLibraryUser(RegisterLibraryUserCommand command);
}

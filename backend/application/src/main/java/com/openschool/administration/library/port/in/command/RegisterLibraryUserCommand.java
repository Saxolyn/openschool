package com.openschool.administration.library.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterLibraryUserCommand {
    private UUID userId;
    private String userType;
    private String libraryCardNumber;
    private Integer maxBooksAllowed;
    private Integer maxRenewalCount;
    private LocalDateTime expiryDate;
    private UUID schoolId;
    private UUID libraryId;
}

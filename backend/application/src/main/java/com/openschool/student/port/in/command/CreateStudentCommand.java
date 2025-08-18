package com.openschool.student.port.in.command;

import com.openschool.domain.student.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateStudentCommand {
    private String studentCode;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String address;
    private UUID schoolId;
    private UUID gradeId;
    private LocalDate enrollmentDate;
}

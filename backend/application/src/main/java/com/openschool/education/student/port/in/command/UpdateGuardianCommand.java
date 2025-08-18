package com.openschool.education.student.port.in.command;

import com.openschool.domain.student.GuardianRelationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateGuardianCommand {
    
    private UUID guardianId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String occupation;
    private GuardianRelationship relationship;
    private String emergencyPhoneNumber;
    private String workPhoneNumber;
}

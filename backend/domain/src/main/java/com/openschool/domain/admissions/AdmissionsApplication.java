package com.openschool.domain.admissions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AdmissionsApplication {
    private UUID id;
    private String applicationNumber;
    
    // Applicant information
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;
    
    // Academic information
    private UUID schoolId;
    private UUID gradeId;
    private UUID academicYearId;
    private String previousSchool;
    private String previousGrade;
    
    // Guardian information
    private String guardianName;
    private String guardianEmail;
    private String guardianPhone;
    private String guardianRelationship;
    
    // Application status
    private AdmissionsStatus status;
    private AdmissionsPriority priority;
    private LocalDateTime submissionDate;
    private LocalDateTime reviewDate;
    private LocalDateTime decisionDate;
    
    // Review process
    private UUID reviewerId;
    private Integer scoreTotal;
    private String reviewNotes;
    private String decisionReason;
    
    // Interview
    private boolean interviewRequired;
    private LocalDateTime interviewDate;
    private UUID interviewerId;
    private String interviewNotes;
    private Integer interviewScore;
    
    // Documents
    private boolean documentsComplete;
    private String missingDocuments;
    private LocalDateTime documentsVerifiedAt;
    
    // Waitlist
    private boolean onWaitlist;
    private Integer waitlistPosition;
    private LocalDateTime waitlistDate;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isSubmitted() {
        return status != AdmissionsStatus.DRAFT;
    }
    
    public boolean canBeReviewed() {
        return status == AdmissionsStatus.SUBMITTED && documentsComplete;
    }
    
    public boolean isApproved() {
        return status == AdmissionsStatus.APPROVED;
    }
    
    public boolean isRejected() {
        return status == AdmissionsStatus.REJECTED;
    }
    
    public void submit() {
        if (status != AdmissionsStatus.DRAFT) {
            throw new IllegalStateException("Application has already been submitted");
        }
        
        this.status = AdmissionsStatus.SUBMITTED;
        this.submissionDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void approve(UUID reviewerId, String reason) {
        this.status = AdmissionsStatus.APPROVED;
        this.reviewerId = reviewerId;
        this.decisionReason = reason;
        this.decisionDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reject(UUID reviewerId, String reason) {
        this.status = AdmissionsStatus.REJECTED;
        this.reviewerId = reviewerId;
        this.decisionReason = reason;
        this.decisionDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void placeOnWaitlist(Integer position) {
        this.status = AdmissionsStatus.WAITLISTED;
        this.onWaitlist = true;
        this.waitlistPosition = position;
        this.waitlistDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void scheduleInterview(LocalDateTime interviewDate, UUID interviewerId) {
        this.interviewRequired = true;
        this.interviewDate = interviewDate;
        this.interviewerId = interviewerId;
        this.status = AdmissionsStatus.INTERVIEW_SCHEDULED;
        this.updatedAt = LocalDateTime.now();
    }
}

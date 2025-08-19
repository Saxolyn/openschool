package com.openschool.infrastructure.config;

import com.openschool.administration.equipment.port.out.EquipmentLoanRepositoryPort;
import com.openschool.administration.equipment.port.out.EquipmentMaintenanceRepositoryPort;
import com.openschool.administration.equipment.port.out.EquipmentRepositoryPort;
import com.openschool.administration.equipment.service.EquipmentService;
import com.openschool.administration.library.port.out.BookLoanRepositoryPort;
import com.openschool.administration.library.port.out.BookRepositoryPort;
import com.openschool.administration.library.port.out.LibraryUserRepositoryPort;
import com.openschool.administration.library.service.LibraryService;
import com.openschool.administration.tuitionFee.port.out.PaymentRepositoryPort;
import com.openschool.administration.tuitionFee.port.out.TuitionFeeRepositoryPort;
import com.openschool.administration.tuitionFee.service.TuitionFeeService;
import com.openschool.administration.costManagement.academicProgram.port.out.BudgetRepositoryPort;
import com.openschool.administration.costManagement.academicProgram.port.out.CostRepositoryPort;
import com.openschool.administration.costManagement.academicProgram.service.AcademicProgramCostService;
import com.openschool.administration.costManagement.admissions.service.AdmissionsCostService;
import com.openschool.administration.costManagement.marketing.service.MarketingCostService;
import com.openschool.administration.costManagement.operations.service.OperationsCostService;
import com.openschool.administration.forum.port.out.ForumThreadRepositoryPort;
import com.openschool.administration.forum.port.out.ForumPostRepositoryPort;
import com.openschool.administration.forum.service.ForumService;
import com.openschool.administration.schedule.port.out.TeacherScheduleRepositoryPort;
import com.openschool.administration.schedule.service.TeacherScheduleService;
import com.openschool.education.exam.port.out.ExamRepositoryPort;
import com.openschool.education.exam.service.ExamService;
import com.openschool.education.mark.port.out.MarkRepositoryPort;
import com.openschool.education.mark.service.MarkService;
import com.openschool.education.admissions.port.out.AdmissionsApplicationRepositoryPort;
import com.openschool.education.admissions.service.AdmissionsService;
import com.openschool.education.studentRecord.port.out.StudentRecordRepositoryPort;
import com.openschool.education.studentRecord.service.StudentRecordService;
import com.openschool.education.teachingMaterial.port.out.TeachingMaterialRepositoryPort;
import com.openschool.education.teachingMaterial.service.TeachingMaterialService;
import com.openschool.education.timetable.port.out.TimetableRepositoryPort;
import com.openschool.education.timetable.service.TimetableService;
import com.openschool.report.education.port.out.ReportRepositoryPort;
import com.openschool.report.education.service.EducationReportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public EquipmentService equipmentService(
            EquipmentRepositoryPort equipmentRepository,
            EquipmentLoanRepositoryPort equipmentLoanRepository,
            EquipmentMaintenanceRepositoryPort equipmentMaintenanceRepository) {
        return new EquipmentService(equipmentRepository, equipmentLoanRepository, equipmentMaintenanceRepository);
    }

    @Bean
    public LibraryService libraryService(
            BookRepositoryPort bookRepository,
            BookLoanRepositoryPort bookLoanRepository,
            LibraryUserRepositoryPort libraryUserRepository) {
        return new LibraryService(bookRepository, bookLoanRepository, libraryUserRepository);
    }

    @Bean
    public TuitionFeeService tuitionFeeService(
            TuitionFeeRepositoryPort tuitionFeeRepository,
            PaymentRepositoryPort paymentRepository) {
        return new TuitionFeeService(tuitionFeeRepository, paymentRepository);
    }

    @Bean
    public AcademicProgramCostService academicProgramCostService(
            CostRepositoryPort costRepository,
            BudgetRepositoryPort budgetRepository) {
        return new AcademicProgramCostService(costRepository, budgetRepository);
    }

    @Bean
    public ExamService examService(ExamRepositoryPort examRepository) {
        return new ExamService(examRepository);
    }

    @Bean
    public AdmissionsCostService admissionsCostService(
            CostRepositoryPort costRepository,
            BudgetRepositoryPort budgetRepository) {
        return new AdmissionsCostService(costRepository, budgetRepository);
    }

    @Bean
    public MarketingCostService marketingCostService(
            CostRepositoryPort costRepository,
            BudgetRepositoryPort budgetRepository) {
        return new MarketingCostService(costRepository, budgetRepository);
    }

    @Bean
    public OperationsCostService operationsCostService(BudgetRepositoryPort budgetRepository) {
        return new OperationsCostService(budgetRepository);
    }

    @Bean
    public ForumService forumService(
            ForumThreadRepositoryPort forumThreadRepository,
            ForumPostRepositoryPort forumPostRepository) {
        return new ForumService(forumThreadRepository, forumPostRepository);
    }

    @Bean
    public TeacherScheduleService teacherScheduleService(TeacherScheduleRepositoryPort teacherScheduleRepository) {
        return new TeacherScheduleService(teacherScheduleRepository);
    }

    @Bean
    public MarkService markService(MarkRepositoryPort markRepository) {
        return new MarkService(markRepository);
    }

    @Bean
    public AdmissionsService admissionsService(AdmissionsApplicationRepositoryPort admissionsRepository) {
        return new AdmissionsService(admissionsRepository);
    }

    @Bean
    public StudentRecordService studentRecordService(StudentRecordRepositoryPort studentRecordRepository) {
        return new StudentRecordService(studentRecordRepository);
    }

    @Bean
    public TeachingMaterialService teachingMaterialService(TeachingMaterialRepositoryPort teachingMaterialRepository) {
        return new TeachingMaterialService(teachingMaterialRepository);
    }

    @Bean
    public TimetableService timetableService(TimetableRepositoryPort timetableRepository) {
        return new TimetableService(timetableRepository);
    }

    @Bean
    public EducationReportService educationReportService(ReportRepositoryPort reportRepository) {
        return new EducationReportService(reportRepository);
    }
}

package controllers;

import interfaces.ApplicantFileHandlerInterface;
import interfaces.EmployeeFileHandlerInterface;
import interfaces.HiringSystemInterface;
import interfaces.JobOpeningFileHandlerInterface;
import models.Applicant;
import models.Employee;
import models.JobOpening;

import java.io.IOException;

public class HiringSystem implements HiringSystemInterface {
    private static final String SUCCESS_MESSAGE               = "Employee hired successfully.";
    private static final String INVALID_DATA_MESSAGE          = "Invalid hiring data entered.";
    private static final String APPLICANT_NOT_FOUND_MESSAGE   = "Applicant does not exist.";
    private static final String APPLICANT_UNAVAILABLE_MESSAGE = "Applicant is not active.";
    private static final String JOB_UNAVAILABLE_MESSAGE       = "Job opening is unavailable.";
    private static final String EMPLOYEE_EXISTS_MESSAGE       = "Employee ID already exists.";
    private static final String FILE_ERROR_MESSAGE            = "Unable to hire employee because of a file error.";

    private final ApplicantFileHandlerInterface applicantFileHandler;
    private final EmployeeFileHandlerInterface employeeFileHandler;
    private final JobOpeningFileHandlerInterface jobOpeningFileHandler;

    public HiringSystem(ApplicantFileHandlerInterface applicantFileHandler,
                        EmployeeFileHandlerInterface employeeFileHandler,
                        JobOpeningFileHandlerInterface jobOpeningFileHandler) {
        this.applicantFileHandler   = applicantFileHandler;
        this.employeeFileHandler    = employeeFileHandler;
        this.jobOpeningFileHandler  = jobOpeningFileHandler;
    }

    @Override
    public String processHiringRequest(String applicantId, String employeeId, String jobOpeningId) {
        if (!validateHiringData(applicantId, employeeId, jobOpeningId)) {
            return INVALID_DATA_MESSAGE;
        }

        applicantId  = applicantId.trim();
        employeeId   = employeeId.trim();
        jobOpeningId = jobOpeningId.trim();

        try {
            Applicant applicant = applicantFileHandler.findApplicantById(applicantId);
            if (applicant == null) return APPLICANT_NOT_FOUND_MESSAGE;
            if (!applicant.isActive()) return APPLICANT_UNAVAILABLE_MESSAGE;

            if (employeeFileHandler.findEmployeeById(employeeId) != null) {
                return EMPLOYEE_EXISTS_MESSAGE;
            }

            JobOpening jobOpening = jobOpeningFileHandler.findJobOpeningById(jobOpeningId);
            if (jobOpening == null || !jobOpening.isAvailable()) {
                return JOB_UNAVAILABLE_MESSAGE;
            }

            Employee employee = new Employee(
                employeeId, applicant.getApplicantName(), jobOpening.getDepartmentName());
            employeeFileHandler.writeEmployee(employee);
            if (!jobOpeningFileHandler.updateJobOpeningStatus(jobOpeningId, "CLOSED")
                    || !applicantFileHandler.updateApplicantStatus(applicantId, "HIRED")) {
                return FILE_ERROR_MESSAGE;
            }
            return SUCCESS_MESSAGE;
        } catch (IOException e) {
            return FILE_ERROR_MESSAGE;
        }
    }

    private boolean validateHiringData(String applicantId, String employeeId, String jobOpeningId) {
        return !isBlank(applicantId) && !isBlank(employeeId) && !isBlank(jobOpeningId);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

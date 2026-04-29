package interfaces;

import models.Applicant;

import java.io.IOException;
import java.util.List;

public interface ApplicantFileHandlerInterface {
    Applicant findApplicantById(String applicantId) throws IOException;
    boolean updateApplicantStatus(String applicantId, String applicantStatus) throws IOException;
    List<Applicant> readAllApplicants() throws IOException;
}

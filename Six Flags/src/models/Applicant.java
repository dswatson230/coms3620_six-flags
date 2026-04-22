package models;

public class Applicant {
    private final String applicantId;
    private final String applicantName;
    private final String applicantStatus;

    public Applicant(String applicantId, String applicantName) {
        this(applicantId, applicantName, "ACTIVE");
    }

    public Applicant(String applicantId, String applicantName, String applicantStatus) {
        this.applicantId   = applicantId;
        this.applicantName = applicantName;
        this.applicantStatus = applicantStatus;
    }

    public String getApplicantId()     { return applicantId; }
    public String getApplicantName()   { return applicantName; }
    public String getApplicantStatus() { return applicantStatus; }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(applicantStatus);
    }

    public Applicant withStatus(String applicantStatus) {
        return new Applicant(applicantId, applicantName, applicantStatus);
    }

    @Override
    public String toString() {
        return "Applicant{id='" + applicantId + "', name='" + applicantName
             + "', status='" + applicantStatus + "'}";
    }
}

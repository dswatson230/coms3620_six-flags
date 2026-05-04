package models;

public class JobOpening {
    private final String jobOpeningId;
    private final String jobTitle;
    private final String departmentName;
    private final String approvalStatus;
    private final String openingStatus;

    public JobOpening(String jobOpeningId, String jobTitle, String departmentName,
                      String approvalStatus, String openingStatus) {
        this.jobOpeningId  = jobOpeningId;
        this.jobTitle      = jobTitle;
        this.departmentName = departmentName;
        this.approvalStatus = approvalStatus;
        this.openingStatus  = openingStatus;
    }

    public String getJobOpeningId()  { return jobOpeningId; }
    public String getJobTitle()      { return jobTitle; }
    public String getDepartmentName() { return departmentName; }
    public String getApprovalStatus() { return approvalStatus; }
    public String getOpeningStatus()  { return openingStatus; }

    public boolean isAvailable() {
        return "APPROVED".equalsIgnoreCase(approvalStatus)
            && "OPEN".equalsIgnoreCase(openingStatus);
    }

    public JobOpening withOpeningStatus(String openingStatus) {
        return new JobOpening(jobOpeningId, jobTitle, departmentName, approvalStatus, openingStatus);
    }

    @Override
    public String toString() {
        return "JobOpening{id='" + jobOpeningId + "', title='" + jobTitle
             + "', department='" + departmentName + "', approval='" + approvalStatus
             + "', status='" + openingStatus + "'}";
    }
}

package models;

public class Issue {
    private final String issueID;
    private final String type;
    private final String description;
    private String status;
    private String resolutionNotes;

    public Issue(String issueID, String type, String description, String status, String resolutionNotes) {
        this.issueID          = issueID;
        this.type             = type;
        this.description      = description;
        this.status           = status;
        this.resolutionNotes  = resolutionNotes;
    }

    public String getIssueID()          { return issueID; }
    public String getType()             { return type; }
    public String getDescription()      { return description; }
    public String getStatus()           { return status; }
    public String getResolutionNotes()  { return resolutionNotes; }

    public void updateStatus(String status)       { this.status = status; }
    public void addResolution(String notes)       { this.resolutionNotes = notes; }

    public String toFileString() {
        return issueID + "|" + type + "|" + description + "|" + status + "|" + resolutionNotes;
    }

    public static Issue fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        return new Issue(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p.length > 4 ? p[4].trim() : "");
    }
}

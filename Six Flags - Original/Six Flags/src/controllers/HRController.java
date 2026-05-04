package controllers;

import data.IssueFileHandler;
import models.Issue;

import java.io.IOException;
import java.util.List;

public class HRController {
    private static final String[] VALID_ACTIONS = {"Resolve", "Document", "Escalate"};
    private final IssueFileHandler issueFileHandler;

    public HRController(String issuesFilePath) {
        this.issueFileHandler = new IssueFileHandler(issuesFilePath);
    }

    public List<Issue> getIssues() throws IOException {
        return issueFileHandler.readIssues();
    }

    public Issue getIssueDetails(String issueID) throws IOException {
        return issueFileHandler.readIssue(issueID);
    }

    public boolean validateAction(String action) {
        if (action == null || action.isBlank()) return false;
        for (String valid : VALID_ACTIONS) {
            if (valid.equalsIgnoreCase(action)) return true;
        }
        return false;
    }

    public void updateIssue(Issue issue, String action, String notes) {
        switch (action) {
            case "Resolve"   -> { issue.updateStatus("Resolved");  issue.addResolution(notes); }
            case "Document"  -> { issue.updateStatus("Documented"); issue.addResolution(notes); }
            case "Escalate"  -> { issue.updateStatus("Escalated"); issue.addResolution("Sent to appropriate department"); }
        }
    }

    public void saveIssue(Issue issue) throws IOException {
        issueFileHandler.writeIssue(issue);
    }
}

package data;

import models.Issue;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class IssueFileHandler {
    private final String filePath;

    public IssueFileHandler(String filePath) {
        this.filePath = filePath;
    }

    public List<Issue> readIssues() throws IOException {
        List<Issue> issues = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return issues;
        for (String line : Files.readAllLines(path)) {
            if (!line.trim().isEmpty()) issues.add(Issue.fromFileString(line));
        }
        return issues;
    }

    public Issue readIssue(String issueID) throws IOException {
        for (Issue issue : readIssues()) {
            if (issue.getIssueID().equals(issueID)) return issue;
        }
        return null;
    }

    public void writeIssue(Issue updated) throws IOException {
        List<Issue> issues = readIssues();
        for (int i = 0; i < issues.size(); i++) {
            if (issues.get(i).getIssueID().equals(updated.getIssueID())) {
                issues.set(i, updated);
                break;
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Issue issue : issues) {
                bw.write(issue.toFileString());
                bw.newLine();
            }
        }
    }
}

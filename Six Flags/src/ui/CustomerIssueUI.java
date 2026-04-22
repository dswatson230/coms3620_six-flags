package ui;

import controllers.HRController;
import models.Issue;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CustomerIssueUI {
    private static final String ISSUES_FILE_PATH = "data/issues.txt";

    private static final String ANSI_BOLD  = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private final HRController  controller;
    private final UserInterface router;
    private final Scanner       scanner;

    public CustomerIssueUI(UserInterface router, Scanner scanner) {
        this.router     = router;
        this.scanner    = scanner;
        this.controller = new HRController(ISSUES_FILE_PATH);
    }

    // Step 1: HR selects manage customer issues
    public void showIssueMenu() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Manage Customer Issues" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. View Issues");
            System.out.println("  2. Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": showIssueList(); break;
                case "2": running = false;  break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option." + ANSI_RESET);
                    router.pause();
            }
        }
    }

    // Step 2 & 3: retrieve and display issues
    private void showIssueList() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();

            List<Issue> issues;
            try {
                issues = controller.getIssues();
            } catch (IOException e) {
                System.out.println(ANSI_RED + "  Could not load issues." + ANSI_RESET);
                router.pause();
                return;
            }

            // Alternate flow 2a: no issues found
            if (issues.isEmpty()) {
                System.out.println("  No issues available.");
                router.pause();
                return;
            }

            System.out.println(ANSI_BOLD + "  Customer Issues" + ANSI_RESET);
            System.out.println("  " + "-".repeat(70));
            System.out.printf("  %-5s %-10s %-14s %-35s%n", "No.", "Issue ID", "Type", "Description");
            System.out.println("  " + "-".repeat(70));
            for (int i = 0; i < issues.size(); i++) {
                Issue issue = issues.get(i);
                String desc = issue.getDescription().length() > 35
                    ? issue.getDescription().substring(0, 32) + "..."
                    : issue.getDescription();
                System.out.printf("  %-5s %-10s %-14s %-35s%n",
                    (i + 1) + ".",
                    issue.getIssueID(),
                    issue.getType(),
                    desc);
            }
            System.out.println("  " + (issues.size() + 1) + ". Back");
            System.out.println("  " + "-".repeat(70));
            System.out.print("  Select an issue: ");

            String choice = scanner.nextLine().trim();

            // Alternate flow 1a: cancel
            if (isCancel(choice)) return;

            int index;
            try {
                index = Integer.parseInt(choice) - 1;
                if (index == issues.size()) return;
                if (index < 0 || index >= issues.size()) {
                    System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                    router.pause();
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid input." + ANSI_RESET);
                router.pause();
                continue;
            }

            // Step 4 & 5: select issue and display details
            showIssueDetails(issues.get(index));
        }
    }

    // Step 5 & 6: display issue details
    private void showIssueDetails(Issue issue) {
        router.clearScreen();
        router.printBanner();
        System.out.println(ANSI_BOLD + "  Issue Details" + ANSI_RESET);
        System.out.println("  " + "-".repeat(50));
        System.out.println("  ID:          " + issue.getIssueID());
        System.out.println("  Type:        " + issue.getType());
        System.out.println("  Status:      " + issue.getStatus());
        System.out.println("  Description: " + issue.getDescription());
        System.out.println("  Resolution:  " + (issue.getResolutionNotes().isEmpty()
            ? "None" : issue.getResolutionNotes()));
        System.out.println("  " + "-".repeat(50));
        System.out.println("  1. Resolve");
        System.out.println("  2. Document");
        System.out.println("  3. Escalate");
        System.out.println("  4. Back");
        System.out.println("  " + "-".repeat(50));
        System.out.print("  Select an action: ");

        String choice = scanner.nextLine().trim();

        // Alternate flow 1a: cancel/back
        if (isCancel(choice) || choice.equals("4")) return;

        String action;
        switch (choice) {
            case "1": action = "Resolve";   break;
            case "2": action = "Document";  break;
            case "3": action = "Escalate";  break;
            default:
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
                return;
        }

        // Alternate flow 7a: escalate goes straight through
        if (action.equals("Escalate")) {
            submitAction(issue, action, "");
            return;
        }

        showNotesInput(issue, action);
    }

    // Step 8 & 9: enter notes and submit
    private void showNotesInput(Issue issue, String action) {
        boolean entering = true;
        while (entering) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  " + action + " Issue: " + issue.getIssueID() + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  Enter C to cancel.");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Enter notes: ");

            String notes = scanner.nextLine().trim();

            // Alternate flow 1a
            if (isCancel(notes)) return;

            // Alternate flow 10a: invalid input
            if (notes.isEmpty()) {
                System.out.println(ANSI_RED + "  Notes cannot be empty." + ANSI_RESET);
                router.pause();
                continue;
            }

            submitAction(issue, action, notes);
            entering = false;
        }
    }

    // Steps 10-13: validate, update, save, confirm
    private void submitAction(Issue issue, String action, String notes) {
        // Step 10: validate
        if (!controller.validateAction(action)) {
            System.out.println(ANSI_RED + "  Invalid action." + ANSI_RESET);
            router.pause();
            return;
        }

        // Step 11: update issue record
        controller.updateIssue(issue, action, notes);

        // Step 12: save to file
        try {
            controller.saveIssue(issue);
        } catch (IOException e) {
            System.out.println(ANSI_RED + "  Error saving issue: " + e.getMessage() + ANSI_RESET);
            router.pause();
            return;
        }

        // Step 13: confirm
        System.out.println();
        System.out.println(ANSI_GREEN + "  Issue " + issue.getIssueID() + " updated to: " + issue.getStatus() + ANSI_RESET);
        router.pause();
    }

    private boolean isCancel(String value) {
        return value.equalsIgnoreCase("C") || value.equalsIgnoreCase("CANCEL");
    }
}
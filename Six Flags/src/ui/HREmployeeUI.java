package ui;

import controllers.HiringController;
import controllers.HiringSystem;
import data.ApplicantFileHandler;
import data.EmployeeFileHandler;
import data.JobOpeningFileHandler;
import interfaces.HiringControllerInterface;
import models.Applicant;
import models.JobOpening;

import java.util.List;
import java.util.Scanner;

public class HREmployeeUI {
    private static final String APPLICANT_FILE_PATH   = "data/applicants.txt";
    private static final String EMPLOYEE_FILE_PATH    = "data/employees.txt";
    private static final String JOB_OPENING_FILE_PATH = "data/job_openings.txt";

    private static final String ANSI_BOLD  = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private final HiringControllerInterface hiringController;
    private final ApplicantFileHandler      applicantFileHandler;
    private final JobOpeningFileHandler     jobOpeningFileHandler;
    private final UserInterface             router;
    private final Scanner                   scanner;

    public HREmployeeUI(UserInterface router, Scanner scanner) {
        this.router               = router;
        this.scanner              = scanner;
        this.applicantFileHandler  = new ApplicantFileHandler(APPLICANT_FILE_PATH);
        EmployeeFileHandler efh    = new EmployeeFileHandler(EMPLOYEE_FILE_PATH);
        this.jobOpeningFileHandler = new JobOpeningFileHandler(JOB_OPENING_FILE_PATH);
        this.hiringController      = new HiringController(
            new HiringSystem(applicantFileHandler, efh, jobOpeningFileHandler));
    }

    // Step 1: start hiring process
    public void startHiringProcess() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  HR -- Hire Employee" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. Hire Employee");
            System.out.println("  2. Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": promptForHireData(); break;
                case "2": running = false;      break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option." + ANSI_RESET);
                    router.pause();
            }
        }
    }

    // Step 2 & 3: prompt for hire data
    private void promptForHireData() {
        boolean entering = true;
        while (entering) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Hire Employee" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));

            showAvailableApplicants();
            showAvailableJobOpenings();

            System.out.println();
            System.out.println("  Enter C at any prompt to cancel.");
            System.out.println("  " + "-".repeat(34));

            String applicantId = readLine("  Applicant ID: ");
            if (isCancel(applicantId)) return;

            String employeeId = readLine("  New Employee ID: ");
            if (isCancel(employeeId)) return;

            String jobOpeningId = readLine("  Job Opening ID: ");
            if (isCancel(jobOpeningId)) return;

            // Step 4: submit — Step 5: system validates
            String result = hiringController.hireEmployee(applicantId, employeeId, jobOpeningId);
            displayResult(result);

            if (result.contains("successfully")
                    || result.equals("Applicant does not exist.")
                    || result.equals("Unable to hire employee because of a file error.")) {
                entering = false;
            }
        }
    }

    // Step 8: display result
    public void displayResult(String message) {
        System.out.println();
        if (message.contains("successfully")) {
            System.out.println(ANSI_GREEN + "  " + message + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "  " + message + ANSI_RESET);
        }
        router.pause();
    }

    private void showAvailableApplicants() {
        List<Applicant> applicants;
        try {
            applicants = applicantFileHandler.readAllApplicants();
        } catch (Exception e) {
            System.out.println(ANSI_RED + "  Could not load applicants." + ANSI_RESET);
            return;
        }

        System.out.println();
        System.out.println(ANSI_BOLD + "  Available Applicants" + ANSI_RESET);
        System.out.println("  " + "-".repeat(50));
        System.out.printf("  %-12s %-24s %-10s%n", "ID", "Name", "Status");
        System.out.println("  " + "-".repeat(50));
        boolean found = false;
        for (Applicant applicant : applicants) {
            if (applicant.isActive()) {
                System.out.printf("  %-12s %-24s %-10s%n",
                    applicant.getApplicantId(),
                    applicant.getApplicantName(),
                    applicant.getApplicantStatus());
                found = true;
            }
        }
        if (!found) System.out.println("  No active applicants found.");
    }

    private void showAvailableJobOpenings() {
        List<JobOpening> jobOpenings;
        try {
            jobOpenings = jobOpeningFileHandler.readAllJobOpenings();
        } catch (Exception e) {
            System.out.println(ANSI_RED + "  Could not load job openings." + ANSI_RESET);
            return;
        }

        System.out.println();
        System.out.println(ANSI_BOLD + "  Available Job Openings" + ANSI_RESET);
        System.out.println("  " + "-".repeat(70));
        System.out.printf("  %-12s %-28s %-22s %-10s%n", "ID", "Title", "Department", "Status");
        System.out.println("  " + "-".repeat(70));
        boolean found = false;
        for (JobOpening jobOpening : jobOpenings) {
            if (jobOpening.isAvailable()) {
                System.out.printf("  %-12s %-28s %-22s %-10s%n",
                    jobOpening.getJobOpeningId(),
                    jobOpening.getJobTitle(),
                    jobOpening.getDepartmentName(),
                    jobOpening.getOpeningStatus());
                found = true;
            }
        }
        if (!found) System.out.println("  No available job openings found.");
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private boolean isCancel(String value) {
        return value.equalsIgnoreCase("C") || value.equalsIgnoreCase("CANCEL");
    }
}
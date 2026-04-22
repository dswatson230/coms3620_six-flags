package ui;

import controllers.HiringController;
import controllers.HiringSystem;
import controllers.SchedulingController;
import controllers.SchedulingSystem;
import data.ApplicantFileHandler;
import data.EmployeeFileHandler;
import data.JobOpeningFileHandler;
import data.ScheduleFileHandler;
import interfaces.HiringControllerInterface;
import interfaces.ScheduleFileHandlerInterface;
import interfaces.SchedulingControllerInterface;
import models.Applicant;
import models.Employee;
import models.EmployeeSchedule;
import models.JobOpening;

import java.util.List;
import java.util.Scanner;

public class SchedulingUI {
    private static final String EMPLOYEE_FILE_PATH    = "data/employees.txt";
    private static final String SCHEDULE_FILE_PATH    = "data/schedules.txt";
    private static final String APPLICANT_FILE_PATH   = "data/applicants.txt";
    private static final String JOB_OPENING_FILE_PATH = "data/job_openings.txt";

    private final SchedulingControllerInterface schedulingController;
    private final HiringControllerInterface hiringController;
    private final ScheduleFileHandlerInterface scheduleFileHandler;
    private final ApplicantFileHandler applicantFileHandler;
    private final EmployeeFileHandler employeeFileHandler;
    private final JobOpeningFileHandler jobOpeningFileHandler;
    private final Scanner scanner;

    public SchedulingUI() {
        this(new Scanner(System.in));
    }

    public SchedulingUI(Scanner scanner) {
        this.scanner               = scanner;
        this.applicantFileHandler  = new ApplicantFileHandler(APPLICANT_FILE_PATH);
        this.employeeFileHandler   = new EmployeeFileHandler(EMPLOYEE_FILE_PATH);
        this.jobOpeningFileHandler = new JobOpeningFileHandler(JOB_OPENING_FILE_PATH);
        this.scheduleFileHandler   = new ScheduleFileHandler(SCHEDULE_FILE_PATH);
        this.schedulingController = new SchedulingController(
            new SchedulingSystem(employeeFileHandler, scheduleFileHandler));
        this.hiringController = new HiringController(
            new HiringSystem(
                applicantFileHandler,
                employeeFileHandler,
                jobOpeningFileHandler));
    }

    public void showSchedulingMenu() {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("Employee Management");
            System.out.println("1. Hire Employee");
            System.out.println("2. Schedule Employee");
            System.out.println("3. View Schedules");
            System.out.println("4. Back");

            String choice = readLine("Select an option: ");
            switch (choice) {
                case "1":
                    showHireEmployeeForm();
                    break;
                case "2":
                    showEmployeeSelection();
                    break;
                case "3":
                    showViewSchedules();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private void showHireEmployeeForm() {
        boolean entering = true;
        while (entering) {
            System.out.println();
            System.out.println("Hire Employee");
            showAvailableApplicants();
            showAvailableJobOpenings();
            System.out.println("Enter C at any prompt to cancel.");

            String applicantId = readLine("Applicant ID: ");
            if (isCancel(applicantId)) return;

            String employeeId = readLine("New Employee ID: ");
            if (isCancel(employeeId)) return;

            String jobOpeningId = readLine("Job Opening ID: ");
            if (isCancel(jobOpeningId)) return;

            String result = hiringController.hireEmployee(applicantId, employeeId, jobOpeningId);
            System.out.println(result);

            if (result.contains("successfully")
                    || result.equals("Applicant does not exist.")
                    || result.equals("Unable to hire employee because of a file error.")) {
                entering = false;
            }
        }
    }

    private void showAvailableApplicants() {
        List<Applicant> applicants;
        try {
            applicants = applicantFileHandler.readAllApplicants();
        } catch (Exception e) {
            System.out.println("Could not load applicants.");
            return;
        }

        System.out.println();
        System.out.println("Available Applicants");
        System.out.printf("%-12s %-24s %-10s%n", "ID", "Name", "Status");
        System.out.println("------------------------------------------------");
        boolean found = false;
        for (Applicant applicant : applicants) {
            if (applicant.isActive()) {
                System.out.printf("%-12s %-24s %-10s%n",
                    applicant.getApplicantId(),
                    applicant.getApplicantName(),
                    applicant.getApplicantStatus());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No active applicants found.");
        }
    }

    private void showAvailableJobOpenings() {
        List<JobOpening> jobOpenings;
        try {
            jobOpenings = jobOpeningFileHandler.readAllJobOpenings();
        } catch (Exception e) {
            System.out.println("Could not load job openings.");
            return;
        }

        System.out.println();
        System.out.println("Available Job Openings");
        System.out.printf("%-12s %-28s %-22s %-10s%n", "ID", "Title", "Department", "Status");
        System.out.println("----------------------------------------------------------------------------");
        boolean found = false;
        for (JobOpening jobOpening : jobOpenings) {
            if (jobOpening.isAvailable()) {
                System.out.printf("%-12s %-28s %-22s %-10s%n",
                    jobOpening.getJobOpeningId(),
                    jobOpening.getJobTitle(),
                    jobOpening.getDepartmentName(),
                    jobOpening.getOpeningStatus());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No available job openings found.");
        }
    }

    private void showEmployeeSelection() {
        List<Employee> employees;
        try {
            employees = employeeFileHandler.readAllEmployees();
        } catch (Exception e) {
            System.out.println("Could not load employees.");
            return;
        }

        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println();
        System.out.println("Select Employee");
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            System.out.println((i + 1) + ". " + employee.getEmployeeName()
                + " - " + employee.getDepartmentName());
        }
        System.out.println("C. Cancel");

        String choice = readLine("Select an employee: ");
        if (isCancel(choice)) return;

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index < 0 || index >= employees.size()) {
                System.out.println("Invalid employee selection.");
                return;
            }
            showScheduleForm(employees.get(index));
        } catch (NumberFormatException e) {
            System.out.println("Invalid employee selection.");
        }
    }

    private void showScheduleForm(Employee employee) {
        boolean entering = true;
        while (entering) {
            System.out.println();
            System.out.println("Scheduling: " + employee.getEmployeeName());
            System.out.println("Enter C at any prompt to cancel.");

            String date = readLine("Date (MM/dd/yyyy): ");
            if (isCancel(date)) return;

            String start = readLine("Start Time (HH:mm): ");
            if (isCancel(start)) return;

            String end = readLine("End Time (HH:mm): ");
            if (isCancel(end)) return;

            String result = schedulingController.manageEmployeeSchedule(
                employee.getEmployeeId(), date, start, end);
            System.out.println(result);

            if (result.contains("successfully")
                    || result.equals("Employee does not exist.")
                    || result.equals("Unable to save schedule because of a file error.")) {
                entering = false;
            }
        }
    }

    private void showViewSchedules() {
        List<EmployeeSchedule> schedules;
        try {
            schedules = scheduleFileHandler.readAllSchedules();
        } catch (Exception e) {
            System.out.println("Could not load schedules.");
            return;
        }

        if (schedules.isEmpty()) {
            System.out.println("No schedules found.");
            return;
        }

        System.out.println();
        System.out.printf("%-20s %-22s %-12s %-10s %-10s%n",
            "Employee", "Department", "Date", "Start", "End");
        System.out.println("----------------------------------------------------------------------------");
        for (EmployeeSchedule schedule : schedules) {
            System.out.printf("%-20s %-22s %-12s %-10s %-10s%n",
                schedule.getEmployeeName(),
                schedule.getDepartmentName(),
                schedule.getScheduleDate(),
                to12Hour(schedule.getStartTime()),
                to12Hour(schedule.getEndTime()));
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private boolean isCancel(String value) {
        return value.equalsIgnoreCase("C") || value.equalsIgnoreCase("CANCEL");
    }

    private String to12Hour(String time24) {
        String[] parts = time24.split(":");
        int hour = Integer.parseInt(parts[0]);
        int min  = Integer.parseInt(parts[1]);
        String amPm = hour < 12 ? "AM" : "PM";
        if (hour == 0) hour = 12;
        else if (hour > 12) hour -= 12;
        return String.format("%d:%02d %s", hour, min, amPm);
    }

    public static void main(String[] args) {
        new SchedulingUI().showSchedulingMenu();
    }
}

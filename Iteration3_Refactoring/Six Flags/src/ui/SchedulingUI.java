package ui;

import controllers.SchedulingController;
import controllers.SchedulingSystem;
import data.EmployeeFileHandler;
import data.ScheduleFileHandler;
import interfaces.ScheduleFileHandlerInterface;
import interfaces.SchedulingControllerInterface;
import models.Employee;
import models.EmployeeSchedule;

import java.util.List;
import java.util.Scanner;

public class SchedulingUI {
    private static final String EMPLOYEE_FILE_PATH = "data/employees.txt";
    private static final String SCHEDULE_FILE_PATH = "data/schedules.txt";
    //private static final String EMPLOYEE_FILE_PATH = "C:/Users/longi/COMS/coms3620/six-flags/Six Flags/data/employees.txt";
    //private static final String SCHEDULE_FILE_PATH = "C:/Users/longi/COMS/coms3620/six-flags/Six Flags/data/schedules.txt";

    private static final String ANSI_BOLD  = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private final SchedulingControllerInterface schedulingController;
    private final ScheduleFileHandlerInterface  scheduleFileHandler;
    private final EmployeeFileHandler           employeeFileHandler;
    private final UserInterface                 router;
    private final Scanner                       scanner;

    public SchedulingUI(Scanner scanner, UserInterface router) {
        this.scanner             = scanner;
        this.router              = router;
        this.employeeFileHandler = new EmployeeFileHandler(EMPLOYEE_FILE_PATH);
        this.scheduleFileHandler = new ScheduleFileHandler(SCHEDULE_FILE_PATH);
        this.schedulingController = new SchedulingController(
            new SchedulingSystem(employeeFileHandler, scheduleFileHandler));
    }

    public void showSchedulingMenu() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Employee Scheduling" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. Schedule Employee");
            System.out.println("  2. View Schedules");
            System.out.println("  3. Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": showEmployeeSelection(); break;
                case "2": showViewSchedules();     break;
                case "3": running = false;         break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option. Please try again." + ANSI_RESET);
                    router.pause();
            }
        }
    }

    private void showEmployeeSelection() {
        List<Employee> employees;
        try {
            employees = employeeFileHandler.readAllEmployees();
        } catch (Exception e) {
            System.out.println(ANSI_RED + "  Could not load employees." + ANSI_RESET);
            router.pause();
            return;
        }

        if (employees.isEmpty()) {
            System.out.println("  No employees found.");
            router.pause();
            return;
        }

        router.clearScreen();
        router.printBanner();
        System.out.println(ANSI_BOLD + "  Select Employee" + ANSI_RESET);
        System.out.println("  " + "-".repeat(50));
        System.out.printf("  %-4s %-25s %-20s%n", "No.", "Name", "Department");
        System.out.println("  " + "-".repeat(50));
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            System.out.printf("  %-4s %-25s %-20s%n",
                (i + 1) + ".",
                employee.getEmployeeName(),
                employee.getDepartmentName());
        }
        System.out.println("  " + (employees.size() + 1) + ". Cancel");
        System.out.println("  " + "-".repeat(50));
        System.out.print("  Select an employee: ");

        String choice = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(choice) - 1;
            if (index == employees.size()) return;
            if (index < 0 || index >= employees.size()) {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
                return;
            }
            showScheduleForm(employees.get(index));
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
            router.pause();
        }
    }

    private void showScheduleForm(Employee employee) {
        boolean entering = true;
        while (entering) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Scheduling: " + employee.getEmployeeName() + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  Enter C at any prompt to cancel.");
            System.out.println("  " + "-".repeat(34));

            String date = readLine("  Date (MM/dd/yyyy): ");
            if (isCancel(date)) return;

            String start = readLine("  Start Time (HH:mm): ");
            if (isCancel(start)) return;

            String end = readLine("  End Time (HH:mm): ");
            if (isCancel(end)) return;

            String result = schedulingController.manageEmployeeSchedule(
                employee.getEmployeeId(), date, start, end);

            if (result.contains("successfully")) {
                System.out.println(ANSI_GREEN + "  " + result + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "  " + result + ANSI_RESET);
            }
            router.pause();

            if (result.contains("successfully")
                    || result.equals("Employee does not exist.")
                    || result.equals("Unable to save schedule because of a file error.")) {
                entering = false;
            }
        }
    }

    private void showViewSchedules() {
        router.clearScreen();
        router.printBanner();
        List<EmployeeSchedule> schedules;
        try {
            schedules = scheduleFileHandler.readAllSchedules();
        } catch (Exception e) {
            System.out.println(ANSI_RED + "  Could not load schedules." + ANSI_RESET);
            router.pause();
            return;
        }

        if (schedules.isEmpty()) {
            System.out.println("  No schedules found.");
            router.pause();
            return;
        }

        System.out.println(ANSI_BOLD + "  Employee Schedules" + ANSI_RESET);
        System.out.println("  " + "-".repeat(74));
        System.out.printf("  %-20s %-22s %-12s %-10s %-10s%n",
            "Employee", "Department", "Date", "Start", "End");
        System.out.println("  " + "-".repeat(74));
        for (EmployeeSchedule schedule : schedules) {
            System.out.printf("  %-20s %-22s %-12s %-10s %-10s%n",
                schedule.getEmployeeName(),
                schedule.getDepartmentName(),
                schedule.getScheduleDate(),
                to12Hour(schedule.getStartTime()),
                to12Hour(schedule.getEndTime()));
        }
        System.out.println("  " + "-".repeat(74));
        router.pause();
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
}
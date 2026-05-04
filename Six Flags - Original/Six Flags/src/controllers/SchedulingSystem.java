package controllers;
 
import interfaces.EmployeeFileHandlerInterface;
import interfaces.ScheduleFileHandlerInterface;
import interfaces.SchedulingSystemInterface;
import models.Employee;
import models.EmployeeSchedule;
 
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
 
public class SchedulingSystem implements SchedulingSystemInterface {
    private static final String SUCCESS_MESSAGE            = "Employee schedule saved successfully.";
    private static final String INVALID_DATA_MESSAGE       = "Invalid scheduling data entered.";
    private static final String EMPLOYEE_NOT_FOUND_MESSAGE = "Employee does not exist.";
    private static final String CONFLICT_MESSAGE           = "Scheduling conflict exists for this employee.";
    private static final String FILE_ERROR_MESSAGE         = "Unable to save schedule because of a file error.";
 
    // American date format: MM/dd/yyyy
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);
 
    private final EmployeeFileHandlerInterface employeeFileHandler;
    private final ScheduleFileHandlerInterface scheduleFileHandler;
 
    public SchedulingSystem(EmployeeFileHandlerInterface employeeFileHandler,
                            ScheduleFileHandlerInterface scheduleFileHandler) {
        this.employeeFileHandler = employeeFileHandler;
        this.scheduleFileHandler = scheduleFileHandler;
    }
 
    @Override
    public String processSchedulingRequest(String employeeId, String scheduleDate, String startTime, String endTime) {
        if (!validateSchedulingData(employeeId, scheduleDate, startTime, endTime)) {
            return INVALID_DATA_MESSAGE;
        }
        try {
            Employee employee = employeeFileHandler.findEmployeeById(employeeId);
            if (employee == null) return EMPLOYEE_NOT_FOUND_MESSAGE;
 
            if (scheduleFileHandler.hasConflict(employeeId, scheduleDate, startTime, endTime)) {
                return CONFLICT_MESSAGE;
            }
 
            EmployeeSchedule schedule = new EmployeeSchedule(
                employee.getEmployeeId(), employee.getEmployeeName(), employee.getDepartmentName(),
                scheduleDate, startTime, endTime);
            scheduleFileHandler.writeSchedule(schedule);
            return SUCCESS_MESSAGE;
        } catch (IOException e) {
            return FILE_ERROR_MESSAGE;
        }
    }
 
    private boolean validateSchedulingData(String employeeId, String scheduleDate, String startTime, String endTime) {
        if (isBlank(employeeId) || isBlank(scheduleDate) || isBlank(startTime) || isBlank(endTime)) return false;
        try {
            LocalDate.parse(scheduleDate, DATE_FORMATTER);
            LocalTime start = LocalTime.parse(startTime, TIME_FORMATTER);
            LocalTime end   = LocalTime.parse(endTime, TIME_FORMATTER);
            return start.isBefore(end);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
 
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
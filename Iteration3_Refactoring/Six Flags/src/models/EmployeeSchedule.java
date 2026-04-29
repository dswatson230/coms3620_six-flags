package models;
 
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
 
public class EmployeeSchedule {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
 
    private final String employeeId;
    private final String employeeName;
    private final String departmentName;
    private final String scheduleDate;
    private final String startTime;
    private final String endTime;
 
    public EmployeeSchedule(String employeeId, String employeeName, String departmentName,
                            String scheduleDate, String startTime, String endTime) {
        this.employeeId     = employeeId;
        this.employeeName   = employeeName;
        this.departmentName = departmentName;
        this.scheduleDate   = scheduleDate;
        this.startTime      = startTime;
        this.endTime        = endTime;
    }
 
    public String getEmployeeId()     { return employeeId; }
    public String getEmployeeName()   { return employeeName; }
    public String getDepartmentName() { return departmentName; }
    public String getScheduleDate()   { return scheduleDate; }
    public String getStartTime()      { return startTime; }
    public String getEndTime()        { return endTime; }
 
    public boolean conflictsWith(String employeeId, String scheduleDate, String startTime, String endTime) {
        if (!this.employeeId.equals(employeeId) || !this.scheduleDate.equals(scheduleDate)) {
            return false;
        }
        try {
            LocalTime existingStart = LocalTime.parse(this.startTime, TIME_FORMATTER);
            LocalTime existingEnd   = LocalTime.parse(this.endTime, TIME_FORMATTER);
            LocalTime newStart      = LocalTime.parse(startTime, TIME_FORMATTER);
            LocalTime newEnd        = LocalTime.parse(endTime, TIME_FORMATTER);
            return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
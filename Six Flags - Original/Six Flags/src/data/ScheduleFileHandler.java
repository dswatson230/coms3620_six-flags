package data;
 
import interfaces.ScheduleFileHandlerInterface;
import models.EmployeeSchedule;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
 
public class ScheduleFileHandler implements ScheduleFileHandlerInterface {
    private final Path scheduleFilePath;
 
    public ScheduleFileHandler(String scheduleFilePath) {
        this.scheduleFilePath = Paths.get(scheduleFilePath);
    }
 
    @Override
    public boolean hasConflict(String employeeId, String scheduleDate, String startTime, String endTime)
            throws IOException {
        for (EmployeeSchedule schedule : readAllSchedules()) {
            if (schedule.conflictsWith(employeeId, scheduleDate, startTime, endTime)) {
                return true;
            }
        }
        return false;
    }
 
    @Override
    public void writeSchedule(EmployeeSchedule employeeSchedule) throws IOException {
        if (scheduleFilePath.getParent() != null) {
            Files.createDirectories(scheduleFilePath.getParent());
        }
        if (Files.notExists(scheduleFilePath)) {
            Files.createFile(scheduleFilePath);
        }
        String record = formatScheduleRecord(employeeSchedule) + System.lineSeparator();
        Files.writeString(scheduleFilePath, record, StandardOpenOption.APPEND);
    }
 
    @Override
    public List<EmployeeSchedule> readAllSchedules() throws IOException {
        List<EmployeeSchedule> schedules = new ArrayList<>();
        if (Files.notExists(scheduleFilePath)) return schedules;
 
        for (String line : Files.readAllLines(scheduleFilePath)) {
            if (line.trim().isEmpty()) continue;
            EmployeeSchedule schedule = parseScheduleRecord(line);
            if (schedule != null) schedules.add(schedule);
        }
        return schedules;
    }
 
    private EmployeeSchedule parseScheduleRecord(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 6) return null;
        return new EmployeeSchedule(
            parts[0].trim(), parts[1].trim(), parts[2].trim(),
            parts[3].trim(), parts[4].trim(), parts[5].trim()
        );
    }
 
    private String formatScheduleRecord(EmployeeSchedule s) {
        return s.getEmployeeId() + "|" + s.getEmployeeName() + "|" + s.getDepartmentName() + "|"
             + s.getScheduleDate() + "|" + s.getStartTime() + "|" + s.getEndTime();
    }
}
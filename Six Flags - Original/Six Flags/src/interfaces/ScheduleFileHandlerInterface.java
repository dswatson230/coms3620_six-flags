package interfaces;
 
import models.EmployeeSchedule;
import java.io.IOException;
import java.util.List;
 
public interface ScheduleFileHandlerInterface {
    boolean hasConflict(String employeeId, String scheduleDate, String startTime, String endTime) throws IOException;
    void writeSchedule(EmployeeSchedule employeeSchedule) throws IOException;
    List<EmployeeSchedule> readAllSchedules() throws IOException;
}
 
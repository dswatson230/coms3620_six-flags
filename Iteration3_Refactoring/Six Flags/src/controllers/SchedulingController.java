package controllers;
 
import interfaces.SchedulingControllerInterface;
import interfaces.SchedulingSystemInterface;
 
public class SchedulingController implements SchedulingControllerInterface {
    private final SchedulingSystemInterface schedulingSystem;
 
    public SchedulingController(SchedulingSystemInterface schedulingSystem) {
        this.schedulingSystem = schedulingSystem;
    }
 
    @Override
    public String manageEmployeeSchedule(String employeeId, String scheduleDate, String startTime, String endTime) {
        return schedulingSystem.processSchedulingRequest(employeeId, scheduleDate, startTime, endTime);
    }
}
 
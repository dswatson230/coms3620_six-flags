package controllers;

import interfaces.HiringControllerInterface;
import interfaces.HiringSystemInterface;

public class HiringController implements HiringControllerInterface {
    private final HiringSystemInterface hiringSystem;

    public HiringController(HiringSystemInterface hiringSystem) {
        this.hiringSystem = hiringSystem;
    }

    @Override
    public String hireEmployee(String applicantId, String employeeId, String jobOpeningId) {
        return hiringSystem.processHiringRequest(applicantId, employeeId, jobOpeningId);
    }
}

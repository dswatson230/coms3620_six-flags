package models;
 
public class Employee {
    private final String employeeId;
    private final String employeeName;
    private final String departmentName;
 
    public Employee(String employeeId, String employeeName, String departmentName) {
        this.employeeId     = employeeId;
        this.employeeName   = employeeName;
        this.departmentName = departmentName;
    }
 
    public String getEmployeeId()     { return employeeId; }
    public String getEmployeeName()   { return employeeName; }
    public String getDepartmentName() { return departmentName; }
 
    @Override
    public String toString() {
        return "Employee{id='" + employeeId + "', name='" + employeeName + "', department='" + departmentName + "'}";
    }
}
 
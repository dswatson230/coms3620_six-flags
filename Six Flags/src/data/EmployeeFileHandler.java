package data;
 
import interfaces.EmployeeFileHandlerInterface;
import models.Employee;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
 
public class EmployeeFileHandler implements EmployeeFileHandlerInterface {
    private final Path employeeFilePath;
 
    public EmployeeFileHandler(String employeeFilePath) {
        this.employeeFilePath = Paths.get(employeeFilePath);
    }
 
    @Override
    public Employee findEmployeeById(String employeeId) throws IOException {
        for (Employee employee : readAllEmployees()) {
            if (employee.getEmployeeId().equals(employeeId)) {
                return employee;
            }
        }
        return null;
    }
 
    @Override
    public List<Employee> readAllEmployees() throws IOException {
        List<Employee> employees = new ArrayList<>();
        if (!Files.exists(employeeFilePath)) return employees;
 
        for (String line : Files.readAllLines(employeeFilePath)) {
            if (line.trim().isEmpty()) continue;
            Employee employee = parseEmployeeRecord(line);
            if (employee != null) employees.add(employee);
        }
        return employees;
    }
 
    private Employee parseEmployeeRecord(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 3) return null;
        return new Employee(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }
}
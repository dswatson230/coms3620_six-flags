package data;
 
import interfaces.EmployeeFileHandlerInterface;
import models.Employee;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
    public void writeEmployee(Employee employee) throws IOException {
        if (employeeFilePath.getParent() != null) {
            Files.createDirectories(employeeFilePath.getParent());
        }
        if (Files.notExists(employeeFilePath)) {
            Files.createFile(employeeFilePath);
        }
        String record = getRecordPrefix() + formatEmployeeRecord(employee) + System.lineSeparator();
        Files.writeString(employeeFilePath, record, StandardOpenOption.APPEND);
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

    private String formatEmployeeRecord(Employee employee) {
        return employee.getEmployeeId() + " | " + employee.getEmployeeName() + " | "
             + employee.getDepartmentName();
    }

    private String getRecordPrefix() throws IOException {
        if (Files.size(employeeFilePath) == 0) return "";

        byte[] bytes = Files.readAllBytes(employeeFilePath);
        byte lastByte = bytes[bytes.length - 1];
        if (lastByte == '\n' || lastByte == '\r') return "";
        return System.lineSeparator();
    }
}

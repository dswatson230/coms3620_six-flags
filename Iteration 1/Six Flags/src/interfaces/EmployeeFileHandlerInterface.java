package interfaces;
 
import models.Employee;
import java.io.IOException;
import java.util.List;
 
public interface EmployeeFileHandlerInterface {
    Employee findEmployeeById(String employeeId) throws IOException;
    List<Employee> readAllEmployees() throws IOException;
}
 
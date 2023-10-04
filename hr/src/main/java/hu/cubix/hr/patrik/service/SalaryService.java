package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Employee;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {

    private EmployeeService employeeService;

    public SalaryService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public void giveNewSalaryForEmployee(Employee employee) {
        employee.setSalary((int) (employee.getSalary() / 100f * (100 + employeeService.getPayRaisePercent(employee))));
    }
}

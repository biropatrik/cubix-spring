package hu.cubix.hr.patrik.controller;

import hu.cubix.hr.patrik.model.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class EmployeeController {

    private List<Employee> employees = new ArrayList<>();

    {
        employees.add(new Employee(1L, "Bryant Chan", "Java Developer", 350, LocalDateTime.now()));
    }

    @GetMapping("/")
    public String getAllEmployees(Map<String, Object> model) {
        model.put("employees", employees);
        model.put("newEmployee", new Employee());
        return "index";
    }

    @GetMapping("/employee/{id}")
    public String getEmployeeById(@PathVariable long id, Map<String, Object> model) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                model.put("employee", employee);
            }
        }
        return "employee";
    }

    @PostMapping("/employee")
    public String createEmployee(Employee employee) {
        employees.add(employee);
        return "redirect:/";
    }

    @PostMapping("/employee/{id}")
    public String updateEmployee(@PathVariable long id, Employee employee) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                emp.setName(employee.getName());
                emp.setJob(employee.getJob());
                emp.setSalary(employee.getSalary());
                emp.setTimestamp(employee.getTimestamp());
            }
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable long id) {
        employees.removeIf(emp -> emp.getId() == id);
        return "redirect:/";
    }
}

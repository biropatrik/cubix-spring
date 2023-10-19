package hu.cubix.hr.patrik.controller;

import hu.cubix.hr.patrik.model.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    private List<Employee> employees = new ArrayList<>();

    {
        employees.add(new Employee(1L, "Java Developer", 350, LocalDateTime.now()));
    }

    @GetMapping("/")
    public String getAllEmployees(Map<String, Object> model) {
        model.put("employees", employees);
        model.put("newEmployee", new Employee());
        return "index";
    }

    @PostMapping("/employee")
    public String createEmployee(Employee employee) {
        employees.add(employee);
        return "redirect:/";
    }
}

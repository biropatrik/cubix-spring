package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEmployeeService implements EmployeeService {

    private Map<Long, Employee> employees = new HashMap<>();

    public Employee create(Employee employee) {
        if (findById(employee.getId()) != null) {
            return null;
        }
        return save(employee);
    }

    public Employee update(Employee employee) {
        if (findById(employee.getId()) == null) {
            return null;
        }
        return save(employee);
    }

    public Employee save(Employee employee) {
        employees.put(employee.getId(), employee);
        return employee;
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    public Employee findById(long id) {
        return employees.get(id);
    }

    public void remove(long id) {
        employees.remove(id);
    }

    public List<Employee> findByHigherSalary(int salary) {
        List<Employee> employeesWithHigherSalary = new ArrayList<>();
        for(Employee employee : employees.values()) {
            if(employee.getSalary() > salary) {
                employeesWithHigherSalary.add(employee);
            }
        }
        return employeesWithHigherSalary;
    }
}

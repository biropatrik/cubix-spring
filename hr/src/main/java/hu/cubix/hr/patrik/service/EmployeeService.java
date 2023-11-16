package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    public Employee save(Employee employee);

    public Employee create(Employee employee);

    public Employee update(Employee employee);

    public Optional<Employee> findById(Long id);

    public List<Employee> findAll();

    public void delete(Long id);

    public int getPayRaisePercent(Employee employee);
}

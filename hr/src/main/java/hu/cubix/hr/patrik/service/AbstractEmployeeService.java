package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractEmployeeService implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyService companyService;

    @Transactional
    public Employee create(Employee employee) {
        if (employeeRepository.existsById(employee.getId())) {
            return null;
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            return null;
        }
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(long id) {
        return employeeRepository.findById(id).get();
    }

    @Transactional
    public void delete(long id) {
        Employee employee = findById(id);
        if (employee.getCompany() != null) {
            companyService.deleteEmployeeFromCompany(employee.getCompany().getId(), id);
        }
        employeeRepository.deleteById(id);
    }

    public List<Employee> findByHigherSalary(int salary) {
        return employeeRepository.findBySalaryGreaterThan(salary);
    }

    public List<Employee> findByJob(String job) {
        return employeeRepository.findByJob(job);
    }

    public List<Employee> findByNameStartingWith(String name) {
        return employeeRepository.findByNameStartingWithIgnoreCase(name);
    }

    public List<Employee> findByEntryDateBetween(LocalDateTime from, LocalDateTime to) {
        return employeeRepository.findByEntryDateBetween(from, to);
    }
}

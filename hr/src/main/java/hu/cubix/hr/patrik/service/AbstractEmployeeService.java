package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.model.Position;
import hu.cubix.hr.patrik.repository.EmployeeRepository;
import hu.cubix.hr.patrik.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEmployeeService implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PositionRepository positionRepository;

    @Override
    public Employee create(Employee employee) {
        if (employeeRepository.existsById(employee.getId())) {
            return null;
        }
        return save(employee);
    }

    @Override
    public Employee save(Employee employee) {
        processPosition(employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            return null;
        }
        return save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> findByHigherSalary(int salary) {
        return employeeRepository.findBySalaryGreaterThan(salary);
    }

    public Page<Employee> findByPosition(String position, Pageable pageable) {
        return employeeRepository.findByPositionName(position, pageable);
    }

    public List<Employee> findByNameStartingWith(String name) {
        return employeeRepository.findByNameStartingWithIgnoreCase(name);
    }

    public List<Employee> findByEntryDateBetween(LocalDateTime from, LocalDateTime to) {
        return employeeRepository.findByEntryDateBetween(from, to);
    }

    private void processPosition(Employee employee) {
        String positionName = employee.getPosition().getName();
        if (positionName != null) {
            List<Position> positions = positionRepository.findByName(positionName);
            Position position = null;
            if (positions.isEmpty()) {
                position = positionRepository.save(new Position(positionName, null));
            } else {
                position = positions.get(0);
            }
            employee.setPosition(position);
        }
    }
}

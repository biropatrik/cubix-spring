package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.model.Position;
import hu.cubix.hr.patrik.repository.EmployeeRepository;
import hu.cubix.hr.patrik.repository.PositionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    @Transactional
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
    @Transactional
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

    public List<Employee> findEmployeesByExample(Employee employee) {

        long id = employee.getId();
        String name = employee.getName();
        String positionName = null;
        int salary = employee.getSalary();
        LocalDate entryDate = null;
        String companyName = null;

        if (employee.getPosition() != null) {
            positionName = employee.getPosition().getName();
        }

        if (employee.getEntryDate() != null) {
            entryDate = employee.getEntryDate().toLocalDate();
        }

        if (employee.getCompany() != null) {
            companyName = employee.getCompany().getName();
        }

        Specification<Employee> specification = Specification.where(null);

        if (id > 0) {
            specification = specification.and(EmployeeSpecifications.hasId(id));
        }

        if (StringUtils.isNotBlank(name)) {
            specification = specification.and(EmployeeSpecifications.nameStartsWith(name));
        }

        if (StringUtils.isNotBlank(positionName)) {
            specification = specification.and(EmployeeSpecifications.positionNamesEquals(positionName));
        }

        if (salary > 0) {
            int percentage = 5;
            int valueByPercentage = calcValueByPercentageOfNumber(percentage, salary);
            int salaryAfter = salary - valueByPercentage;
            int salaryBefore = salary + valueByPercentage;

            specification = specification.and(EmployeeSpecifications.salaryBetween(salaryAfter, salaryBefore));
        }

        if (entryDate != null) {
            specification = specification.and(EmployeeSpecifications.entryDateEquals(entryDate));
        }

        if (StringUtils.isNotBlank(companyName)) {
            specification = specification.and(EmployeeSpecifications.companyNameStartsWith(companyName));
        }

        return employeeRepository.findAll(specification);
    }

    private int calcValueByPercentageOfNumber(int percentage, int number) {
        return (int) (number * (percentage / 100f));
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

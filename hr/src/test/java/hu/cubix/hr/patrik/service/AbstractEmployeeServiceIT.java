package hu.cubix.hr.patrik.service;

import static org.assertj.core.api.Assertions.assertThat;

import hu.cubix.hr.patrik.model.Company;
import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.model.Position;
import hu.cubix.hr.patrik.model.Qualification;
import hu.cubix.hr.patrik.repository.CompanyRepository;
import hu.cubix.hr.patrik.repository.EmployeeRepository;
import hu.cubix.hr.patrik.repository.PositionDetailsByCompanyRepository;
import hu.cubix.hr.patrik.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
public class AbstractEmployeeServiceIT {

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AbstractEmployeeService employeeService;

    @BeforeEach
    void init() {
        employeeRepository.deleteAllInBatch();
        positionDetailsByCompanyRepository.deleteAllInBatch();
        positionRepository.deleteAllInBatch();
        companyRepository.deleteAllInBatch();
    }

    @Test
    void testFindEmployeesByExample_FindById() {
        LocalDateTime now = LocalDateTime.now();
        Position position = positionRepository.save(new Position("Java Developer", Qualification.HIGH_SCHOOL));
        Employee employee1 = employeeRepository.save(new Employee("Test", position, 700, now));
        Employee employee2 = employeeRepository.save(new Employee("John", position, 900, now));

        Employee example = new Employee(employee1.getId(), null, null, 0, null);
        List<Employee> actualList = employeeService.findEmployeesByExample(example);

        assertThat(actualList).containsExactlyInAnyOrder(employee1);
    }

    @Test
    void testFindEmployeesByExample_FindByName() {
        LocalDateTime now = LocalDateTime.now();
        Position position = positionRepository.save(new Position("Java Developer", Qualification.HIGH_SCHOOL));
        Employee employee1 = employeeRepository.save(new Employee("Test", position, 700, now));
        Employee employee2 = employeeRepository.save(new Employee("John", position, 900, now));
        Employee employee3 = employeeRepository.save(new Employee("Test 2", position, 900, now));

        Employee example = new Employee("T", null, 0, null);
        List<Employee> actualList = employeeService.findEmployeesByExample(example);

        assertThat(actualList).containsExactlyInAnyOrder(employee1, employee3);
    }

    @Test
    void testFindEmployeesByExample_FindByPositionName() {
        LocalDateTime now = LocalDateTime.now();
        Position position1 = positionRepository.save(new Position("Java Developer", Qualification.HIGH_SCHOOL));
        Position position2 = positionRepository.save(new Position("Java BE", Qualification.COLLEGE));
        Employee employee1 = employeeRepository.save(new Employee("Test", position1, 700, now));
        Employee employee2 = employeeRepository.save(new Employee("John", position1, 900, now));
        Employee employee3 = employeeRepository.save(new Employee("Test 2", position2, 900, now));

        Employee example = new Employee(null, position1, 0, null);
        List<Employee> actualList = employeeService.findEmployeesByExample(example);

        assertThat(actualList).containsExactlyInAnyOrder(employee1, employee2);
    }

    @Test
    void testFindEmployeesByExample_FindBySalary() {
        LocalDateTime now = LocalDateTime.now();
        Position position = positionRepository.save(new Position("Java Developer", Qualification.HIGH_SCHOOL));
        Employee employee1 = employeeRepository.save(new Employee("Test", position, 665, now));
        Employee employee2 = employeeRepository.save(new Employee("John", position, 736, now));
        Employee employee3 = employeeRepository.save(new Employee("Test 2", position, 735, now));
        Employee employee4 = employeeRepository.save(new Employee("Test 3", position, 664, now));

        Employee example = new Employee(null, null, 700, null);
        List<Employee> actualList = employeeService.findEmployeesByExample(example);

        assertThat(actualList).containsExactlyInAnyOrder(employee1, employee3);
    }

    @Test
    void testFindEmployeesByExample_FindByEntryDate() {
        LocalDateTime dateTime1 = LocalDateTime.now();
        LocalDateTime dateTime2 = dateTime1.minusDays(1);
        Position position = positionRepository.save(new Position("Java Developer", Qualification.HIGH_SCHOOL));
        Employee employee1 = employeeRepository.save(new Employee("Test", position, 700, dateTime1));
        Employee employee2 = employeeRepository.save(new Employee("John", position, 900, dateTime2));
        Employee employee3 = employeeRepository.save(new Employee("Test 2", position, 900, dateTime1));

        Employee example = new Employee(null, null, 0, dateTime2);
        List<Employee> actualList = employeeService.findEmployeesByExample(example);

        assertThat(actualList).containsExactlyInAnyOrder(employee2);
    }

    @Test
    void testFindEmployeesByExample_FindByCompanyName() {
        LocalDateTime now = LocalDateTime.now();
        Position position = positionRepository.save(new Position("Java Developer", Qualification.HIGH_SCHOOL));
        Employee employee1 = new Employee("Test", position, 700, now);
        Employee employee2 = new Employee("John", position, 900, now);
        Employee employee3 = new Employee("Test 2", position, 900, now);

        Company company1 = companyRepository.save(new Company(12345, "Start", "3300 Eger",
                List.of(employee1)));
        Company company2 = companyRepository.save(new Company(23456, "Sure", "4000 Debrecen",
                List.of(employee2, employee3)));

        employeeRepository.saveAll(List.of(employee1, employee2, employee3));

        Employee example = new Employee(0, null, null, 0, null, new Company("St"));
        List<Employee> actualList = employeeService.findEmployeesByExample(example);

        assertThat(actualList).containsExactlyInAnyOrder(employee1);
    }
}

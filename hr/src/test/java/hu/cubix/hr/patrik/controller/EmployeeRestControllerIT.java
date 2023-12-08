package hu.cubix.hr.patrik.controller;

import static org.assertj.core.api.Assertions.assertThat;

import hu.cubix.hr.patrik.dto.EmployeeDto;
import hu.cubix.hr.patrik.dto.LoginDto;
import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.repository.EmployeeRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeRestControllerIT {

    private static final String API_EMPLOYEES = "/api/employees";
    private static final String API_LOGIN = "/api/login";
    private static final String EMPLOYEE_NAME = "Teszt Name";
    private static final String EMPLOYEE_JOB = "Teszt Job";
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "pass";
    private static final int EMPLOYEE_SALARY = 1200;
    private static final LocalDateTime EMPLOYEE_ENTRY = LocalDateTime.of(2000,10,10,10,0);


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    public void init() {
        if (employeeRepository.findByUsername(USERNAME).isEmpty()) {
            Employee testuser = new Employee();
            testuser.setUsername(USERNAME);
            testuser.setPassword(passwordEncoder.encode(PASSWORD));
            employeeRepository.save(testuser);
        }

        var login = new LoginDto();
        login.setUsername(USERNAME);
        login.setPassword(PASSWORD);

        this.token = webTestClient.post()
                .uri(API_LOGIN)
                .bodyValue(login)
                .exchange()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void testThatCreatedEmployeeIsListed() {
        List<EmployeeDto> employeeBefore = getAllEmployees();
        EmployeeDto newEmployee = new EmployeeDto(
                getLastIdForEmployee(), EMPLOYEE_NAME, EMPLOYEE_JOB, EMPLOYEE_SALARY, EMPLOYEE_ENTRY);

        createEmployee(newEmployee);
        List<EmployeeDto> employeeAfter = getAllEmployees();

        assertThat(employeeAfter.subList(0, employeeBefore.size()))
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(employeeBefore);

        assertThat(employeeAfter.get(employeeAfter.size() - 1))
                .usingRecursiveComparison()
                .isEqualTo(newEmployee);
    }

    @Test
    void testCreateEmployeeWithInvalidName() {
        EmployeeDto employeeWithInvalidName = new EmployeeDto(
                getLastIdForEmployee(), StringUtils.EMPTY, EMPLOYEE_JOB, EMPLOYEE_SALARY, EMPLOYEE_ENTRY);

        webTestClient
            .post()
            .uri(API_EMPLOYEES)
            .headers(h -> h.setBearerAuth(token))
            .bodyValue(employeeWithInvalidName)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void testCreateEmployeeWithInvalidSalary() {
        EmployeeDto employeeWithInvalidName = new EmployeeDto(
                getLastIdForEmployee(), EMPLOYEE_NAME, EMPLOYEE_JOB, -200, EMPLOYEE_ENTRY);

        webTestClient
                .post()
                .uri(API_EMPLOYEES)
                .headers(h -> h.setBearerAuth(token))
                .bodyValue(employeeWithInvalidName)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateEmployeeWithInvalidJob() {
        EmployeeDto employeeWithInvalidName = new EmployeeDto(
                getLastIdForEmployee(), EMPLOYEE_NAME, StringUtils.EMPTY, EMPLOYEE_SALARY, EMPLOYEE_ENTRY);

        webTestClient
                .post()
                .uri(API_EMPLOYEES)
                .headers(h -> h.setBearerAuth(token))
                .bodyValue(employeeWithInvalidName)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateEmployee() {
        long id = getLastIdForEmployee();
        EmployeeDto newEmployee = new EmployeeDto(
                id, EMPLOYEE_NAME, EMPLOYEE_JOB, EMPLOYEE_SALARY, EMPLOYEE_ENTRY);

        createEmployee(newEmployee);
        List<EmployeeDto> employeeBefore = getAllEmployees();

        String modifiedName = "New Name";
        EmployeeDto modifiedEmployee = new EmployeeDto(
                id, modifiedName, EMPLOYEE_JOB, EMPLOYEE_SALARY, EMPLOYEE_ENTRY);
        updateEmployee(modifiedEmployee);
        List<EmployeeDto> employeeAfter = getAllEmployees();

        assertThat(employeeBefore.get(employeeBefore.size() - 1))
                .usingRecursiveComparison()
                .isEqualTo(newEmployee);

        assertThat(employeeAfter.get(employeeAfter.size() - 1))
                .usingRecursiveComparison()
                .isEqualTo(modifiedEmployee);
    }

    @Test
    void testUpdateEmployeeWithInvalidName() {
        long id = getLastIdForEmployee();
        EmployeeDto newEmployee = new EmployeeDto(
                id, EMPLOYEE_NAME, EMPLOYEE_JOB, EMPLOYEE_SALARY, EMPLOYEE_ENTRY);

        createEmployee(newEmployee);

        String modifiedName = StringUtils.EMPTY;
        EmployeeDto modifiedEmployee = new EmployeeDto(
                id, modifiedName, EMPLOYEE_JOB, EMPLOYEE_SALARY, EMPLOYEE_ENTRY);

        webTestClient
            .put()
            .uri(API_EMPLOYEES + "/{id}", modifiedEmployee.getId())
            .headers(h -> h.setBearerAuth(token))
            .bodyValue(modifiedEmployee)
            .exchange()
            .expectStatus().isBadRequest();
    }

    private long getLastIdForEmployee() {
        List<EmployeeDto> employees = getAllEmployees();
        if(!employees.isEmpty()) {
            return employees.get(employees.size() - 1).getId() + 1;
        } else {
            return 1;
        }
    }

    private void createEmployee(EmployeeDto employeeDto) {
        webTestClient
            .post()
            .uri(API_EMPLOYEES)
            .headers(h -> h.setBearerAuth(token))
            .bodyValue(employeeDto)
            .exchange()
            .expectStatus().isOk();
    }

    private void updateEmployee(EmployeeDto employeeDto) {
        webTestClient
            .put()
            .uri(API_EMPLOYEES + "/{id}", employeeDto.getId())
            .headers(h -> h.setBearerAuth(token))
            .bodyValue(employeeDto)
            .exchange()
            .expectStatus().isOk();
    }

    private List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> allEmployees = webTestClient
            .get()
            .uri(API_EMPLOYEES)
            .headers(h -> h.setBearerAuth(token))
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(EmployeeDto.class)
            .returnResult()
            .getResponseBody();

        Collections.sort(allEmployees, Comparator.comparing(EmployeeDto::getId));

        return allEmployees;
    }
}

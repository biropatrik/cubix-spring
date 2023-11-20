package hu.cubix.hr.patrik.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.cubix.hr.patrik.dto.CompanyDto;
import hu.cubix.hr.patrik.dto.EmployeeDto;
import hu.cubix.hr.patrik.model.Company;
import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.model.Position;
import hu.cubix.hr.patrik.model.Qualification;
import hu.cubix.hr.patrik.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class CompanyRestControllerIT {

    private static final String API_COMPANIES_ADD_NEW_EMPLOYEES = "/api/companies/{id}/employees";
    private static final String API_COMPANIES_DELETE_EMPLOYEE_FROM_COMPANY = "/api/companies/{id}/employees/{employeeId}";
    private static final String API_COMPANIES_REPLACE_EMPLOYEES = "/api/companies/{id}/employees";
    private static long companyId;
    private static long employeeId;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @BeforeEach
    void init() {
        employeeRepository.deleteAll();
        positionDetailsByCompanyRepository.deleteAll();
        positionRepository.deleteAll();
        companyRepository.deleteAll();

        Position position = new Position("Java Developer", Qualification.HIGH_SCHOOL);
        Employee employee = new Employee("John Doe", position, 900, LocalDateTime.now());
        Company company = new Company(12345, "SW", "3300 Eger", List.of(employee), null);

        positionRepository.save(position);
        companyId = companyRepository.save(company).getId();
        employeeId = employeeRepository.save(employee).getId();
    }

    @Test
    void testAddNewEmployee() {
        Position position = positionRepository.findAll().get(0);
        String name = "John Test";
        EmployeeDto employeeDto = new EmployeeDto(name, position.getName(), 800, LocalDateTime.of(2020,10,10,8,0));

         webTestClient
                .post()
                .uri(API_COMPANIES_ADD_NEW_EMPLOYEES, companyId)
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().isOk();

         List<CompanyDto> actual = getAllCompanies();
         EmployeeDto actualEmp = actual.get(0).getEmployees().get(1);

         assertEquals(2, actual.get(0).getEmployees().size());
         assertEquals(name, actualEmp.getName());
         assertEquals(position.getName(), actualEmp.getPositionName());
    }

    @Test
    void testDeleteEmployeeFromCompany() {
        webTestClient
                .delete()
                .uri(API_COMPANIES_DELETE_EMPLOYEE_FROM_COMPANY, companyId, employeeId)
                .exchange()
                .expectStatus().isOk();

        List<CompanyDto> actual = getAllCompanies();
        assertEquals(0, actual.get(0).getEmployees().size());
    }

    @Test
    void testReplaceEmployees() {
        Position position = positionRepository.findAll().get(0);
        LocalDateTime now = LocalDateTime.now();
        String name1 = "Test 1";
        String name2 = "Test 2";
        EmployeeDto emp1 = new EmployeeDto(name1, position.getName(), 700, now);
        EmployeeDto emp2 = new EmployeeDto(name2, position.getName(), 900, now);

        webTestClient
                .put()
                .uri(API_COMPANIES_REPLACE_EMPLOYEES, companyId)
                .bodyValue(List.of(emp1, emp2))
                .exchange()
                .expectStatus().isOk();

        List<CompanyDto> actualList = getAllCompanies();
        EmployeeDto actualEmp1 = actualList.get(0).getEmployees().get(0);
        EmployeeDto actualEmp2 = actualList.get(0).getEmployees().get(1);

        assertEquals(2, actualList.get(0).getEmployees().size());
        assertEquals(name1, actualEmp1.getName());
        assertEquals(name2, actualEmp2.getName());
        assertEquals(position.getName(), actualEmp1.getPositionName());
        assertEquals(position.getName(), actualEmp2.getPositionName());
    }

    private List<CompanyDto> getAllCompanies() {
        List<CompanyDto> allCompanies = webTestClient
                .get()
                .uri("/api/companies?full=true")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CompanyDto.class)
                .returnResult()
                .getResponseBody();

        Collections.sort(allCompanies, Comparator.comparing(CompanyDto::getId));
        return allCompanies;
    }
}

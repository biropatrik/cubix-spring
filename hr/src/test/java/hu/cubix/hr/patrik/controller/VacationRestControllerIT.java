package hu.cubix.hr.patrik.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.cubix.hr.patrik.dto.VacationDto;
import hu.cubix.hr.patrik.dto.VacationInsertDto;
import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.model.Vacation;
import hu.cubix.hr.patrik.model.VacationStatus;
import hu.cubix.hr.patrik.repository.EmployeeRepository;
import hu.cubix.hr.patrik.repository.VacationRepository;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class VacationRestControllerIT {

    private static final String API_VACATIONS = "/api/vacations";
    private static final String API_VACATIONS_CREATE = "/api/vacations";
    private static final String API_VACATIONS_FIND_BY_ID = "/api/vacations/{id}";
    private static final String API_VACATIONS_MODIFY_VACATION = "/api/vacations/modify/{id}";
    private static final String API_VACATIONS_MANAGE_VACATION = "/api/vacations/manage/{id}";
    private static final String VACATION_ALREADY_PROCESSED_MESSAGE = "Vacation request already processed!";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    VacationRepository vacationRepository;

    @BeforeEach
    void init() {
        vacationRepository.deleteAllInBatch();
        employeeRepository.deleteAllInBatch();
    }

    @Test
    void testCreate_Ok() {
        Employee employee = employeeRepository.save(new Employee());
        LocalDateTime startDate = LocalDateTime.of(2025, 10, 10, 8, 0);
        LocalDateTime endDate = startDate.plusDays(2);
        VacationInsertDto insertDto = new VacationInsertDto(startDate, endDate, employee.getId());

        List<Vacation> vacationsBefore = vacationRepository.findAll();

        createVacation(insertDto);

        List<Vacation> vacationsAfter = vacationRepository.findAll();

        assertEquals(0, vacationsBefore.size());
        assertEquals(1, vacationsAfter.size());
        assertEquals(startDate, vacationsAfter.get(0).getStartDate());
    }

    @Test
    void testCreate_BadRequest() {
        Employee employee = employeeRepository.save(new Employee());
        LocalDateTime pastDate = LocalDateTime.of(2020,10,10,8,0);
        VacationInsertDto insertDto = new VacationInsertDto(pastDate, pastDate, employee.getId());

        webTestClient
                .post()
                .uri(API_VACATIONS_CREATE)
                .bodyValue(insertDto)
                .exchange()
                .expectStatus().isBadRequest();

        LocalDateTime startDate = LocalDateTime.of(2025,10,10,8,0);
        LocalDateTime endDateBeforeStartDate = startDate.minusDays(1);
        insertDto = new VacationInsertDto(startDate, endDateBeforeStartDate, employee.getId());

        webTestClient
                .post()
                .uri(API_VACATIONS_CREATE)
                .bodyValue(insertDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testFindAll() {
        Employee employee1 = employeeRepository.save(new Employee());
        LocalDateTime startDate1 = LocalDateTime.of(2025, 10, 10, 8, 0);
        LocalDateTime endDate1 = startDate1.plusDays(2);
        VacationInsertDto insertDto1 = new VacationInsertDto(startDate1, endDate1, employee1.getId());

        Employee employee2 = employeeRepository.save(new Employee());
        LocalDateTime startDate2 = LocalDateTime.of(2025, 12, 10, 8, 0);
        LocalDateTime endDate2 = startDate2.plusDays(5);
        VacationInsertDto insertDto2 = new VacationInsertDto(startDate2, endDate2, employee2.getId());

        createVacation(insertDto1);
        createVacation(insertDto2);

        List<VacationDto> vacationDtos = webTestClient
                .get()
                .uri(API_VACATIONS)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VacationDto.class)
                .returnResult()
                .getResponseBody();

        assert vacationDtos != null;
        assertEquals(2, vacationDtos.size());
    }

    @Test
    void testFindAll_SpecificationNamePrefix() {
        String name1 = "Test";
        Employee employee1 = employeeRepository.save(new Employee(name1));
        LocalDateTime startDate1 = LocalDateTime.of(2025, 10, 10, 8, 0);
        LocalDateTime endDate1 = startDate1.plusDays(2);
        VacationInsertDto insertDto1 = new VacationInsertDto(startDate1, endDate1, employee1.getId());

        Employee employee2 = employeeRepository.save(new Employee("John"));
        LocalDateTime startDate2 = LocalDateTime.of(2025, 12, 10, 8, 0);
        LocalDateTime endDate2 = startDate2.plusDays(5);
        VacationInsertDto insertDto2 = new VacationInsertDto(startDate2, endDate2, employee2.getId());

        createVacation(insertDto1);
        createVacation(insertDto2);

        List<VacationDto> vacationDtos1 = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(API_VACATIONS)
                        .queryParam("requesterNamePrefix", "T")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VacationDto.class)
                .returnResult()
                .getResponseBody();

        assert vacationDtos1 != null;
        assertEquals(1, vacationDtos1.size());
        assertEquals(name1, vacationDtos1.get(0).getRequester().getName());
    }

    @Test
    void testFindAll_SpecificationStartDate() {
        Employee employee1 = employeeRepository.save(new Employee("John"));
        LocalDateTime startDate1 = LocalDateTime.of(2025, 10, 10, 8, 0);
        LocalDateTime endDate1 = startDate1.plusDays(2);
        VacationInsertDto insertDto1 = new VacationInsertDto(startDate1, endDate1, employee1.getId());

        Employee employee2 = employeeRepository.save(new Employee("John"));
        LocalDateTime startDate2 = LocalDateTime.of(2025, 12, 10, 8, 0);
        LocalDateTime endDate2 = startDate2.plusDays(5);
        VacationInsertDto insertDto2 = new VacationInsertDto(startDate2, endDate2, employee2.getId());

        createVacation(insertDto1);
        createVacation(insertDto2);

        LocalDateTime startDate = startDate1.minusDays(5);
        LocalDateTime endDate = startDate1.plusDays(1);
        List<VacationDto> vacationDtos = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(API_VACATIONS)
                        .queryParam("startDate", startDate)
                        .queryParam("endDate", endDate)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VacationDto.class)
                .returnResult()
                .getResponseBody();

        assert vacationDtos != null;
        assertEquals(1, vacationDtos.size());
        assertEquals(startDate1, vacationDtos.get(0).getStartDate());
    }

    @Test
    void testFindById() {
        Employee employee = employeeRepository.save(new Employee());
        LocalDateTime startDate = LocalDateTime.of(2025, 10, 10, 8, 0);
        LocalDateTime endDate = startDate.plusDays(2);
        VacationInsertDto insertDto = new VacationInsertDto(startDate, endDate, employee.getId());

        VacationDto vacationDto = createVacation(insertDto);

        webTestClient
                .get()
                .uri(API_VACATIONS_FIND_BY_ID, vacationDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(VacationDto.class)
                .consumeWith((result -> assertEquals(vacationDto.getId(),
                        Objects.requireNonNull(result.getResponseBody()).getId())));
    }

    @Test
    void testModifyVacation_NotManaged() {
        Employee employee = employeeRepository.save(new Employee());
        LocalDateTime startDate = LocalDateTime.of(2025, 10, 10, 8, 0);
        LocalDateTime endDate = startDate.plusDays(2);
        VacationInsertDto insertDto = new VacationInsertDto(startDate, endDate, employee.getId());

        VacationDto vacationDto = createVacation(insertDto);

        LocalDateTime modifiedEndDate = endDate.plusDays(5);
        insertDto.setEndDate(modifiedEndDate);

        webTestClient
                .put()
                .uri(API_VACATIONS_MODIFY_VACATION, vacationDto.getId())
                .bodyValue(insertDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(VacationDto.class)
                .consumeWith((result -> assertEquals(modifiedEndDate,
                        Objects.requireNonNull(result.getResponseBody()).getEndDate())));
    }

    @Test
    void testModifyVacation_Managed() {
        Employee employee = employeeRepository.save(new Employee());
        LocalDateTime startDate = LocalDateTime.of(2025, 10, 10, 8, 0);
        LocalDateTime endDate = startDate.plusDays(2);
        VacationInsertDto insertDto = new VacationInsertDto(startDate, endDate, employee.getId());

        VacationDto vacationDto = createVacation(insertDto);

        //Manage
        VacationStatus managedStatus = VacationStatus.DECLINED;
        Employee manager = employeeRepository.save(new Employee());
        webTestClient
                .put()
                .uri(uriBuilder -> uriBuilder
                        .path(API_VACATIONS_MANAGE_VACATION)
                        .queryParam("status", managedStatus)
                        .queryParam("managerOfEmployee", manager.getId())
                        .build(vacationDto.getId()))
                .exchange()
                .expectStatus().isOk();

        //Modify
        LocalDateTime modifiedEndDate = endDate.plusDays(5);
        insertDto.setEndDate(modifiedEndDate);
        webTestClient
                .put()
                .uri(API_VACATIONS_MODIFY_VACATION, vacationDto.getId())
                .bodyValue(insertDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Response.class)
                .consumeWith(exp -> assertEquals(VACATION_ALREADY_PROCESSED_MESSAGE,
                        Objects.requireNonNull(exp.getResponseBody()).getMessage()));
    }

    @Test
    void testManageVacation() {
        Employee employee = employeeRepository.save(new Employee());
        LocalDateTime startDate = LocalDateTime.of(2025, 10, 10, 8, 0);
        LocalDateTime endDate = startDate.plusDays(2);
        VacationInsertDto insertDto = new VacationInsertDto(startDate, endDate, employee.getId());

        VacationDto vacationDto = createVacation(insertDto);

        //Manage
        VacationStatus managedStatus = VacationStatus.DECLINED;
        Employee manager = employeeRepository.save(new Employee());
        webTestClient
                .put()
                .uri(uriBuilder -> uriBuilder
                        .path(API_VACATIONS_MANAGE_VACATION)
                        .queryParam("status", managedStatus)
                        .queryParam("managerOfEmployee", manager.getId())
                        .build(vacationDto.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(VacationDto.class)
                .consumeWith(result -> assertEquals(managedStatus,
                        Objects.requireNonNull(result.getResponseBody()).getStatus()));
    }

    private VacationDto createVacation(VacationInsertDto insertDto) {
        VacationDto vacationDto = webTestClient
                .post()
                .uri(API_VACATIONS_CREATE)
                .bodyValue(insertDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(VacationDto.class)
                .returnResult()
                .getResponseBody();

        assert vacationDto != null;
        return vacationDto;
    }
}

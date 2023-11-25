package hu.cubix.hr.patrik.controller;

import hu.cubix.hr.patrik.dto.VacationDto;
import hu.cubix.hr.patrik.dto.VacationExampleDto;
import hu.cubix.hr.patrik.dto.VacationInsertDto;
import hu.cubix.hr.patrik.mapper.VacationMapper;
import hu.cubix.hr.patrik.model.VacationStatus;
import hu.cubix.hr.patrik.model.Vacation_;
import hu.cubix.hr.patrik.service.VacationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vacations")
public class VacationRestController {

    @Autowired
    VacationService vacationService;

    @Autowired
    VacationMapper vacationMapper;

    @PostMapping()
    public VacationDto create(@RequestBody @Valid VacationInsertDto vacationInsertDto) {
        return vacationMapper.vacationToDto(vacationService.create(vacationInsertDto));
    }

    @GetMapping()
    public List<VacationDto> findAll(
            @SortDefault(Vacation_.START_DATE) Pageable pageable,
            @RequestParam(required = false) VacationStatus vacationStatus,
            @RequestParam(required = false) String requesterNamePrefix,
            @RequestParam(required = false) String managerNamePrefix,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDate insertedDateFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDate insertedDateTo,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate
            ) {
        VacationExampleDto exampleDto = new VacationExampleDto(
                vacationStatus,
                requesterNamePrefix,
                managerNamePrefix,
                insertedDateFrom,
                insertedDateTo,
                startDate,
                endDate
        );

        return vacationMapper.vacationsToDtos(
                vacationService.findVacationsByExample(exampleDto, pageable).getContent());
    }

    @GetMapping("/{id}")
    public VacationDto findById(@PathVariable long id) {
        return vacationMapper.vacationToDto(vacationService.findById(id));
    }

    @PutMapping("/modify/{id}")
    public VacationDto modifyVacation(@PathVariable long id, @RequestBody @Valid VacationInsertDto vacationInsertDto) {
        return vacationMapper.vacationToDto(vacationService.modifyVacation(id, vacationInsertDto));
    }

    @PutMapping("/manage/{id}")
    public VacationDto manageVacation(
            @PathVariable long id,
            @RequestParam VacationStatus status,
            @RequestParam long managerOfEmployee) {

        return vacationMapper.vacationToDto(vacationService.manageVacation(id, status, managerOfEmployee));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        vacationService.delete(id);
    }
}

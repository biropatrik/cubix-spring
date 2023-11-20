package hu.cubix.hr.patrik.controller;

import hu.cubix.hr.patrik.dto.CompanyDto;
import hu.cubix.hr.patrik.dto.EmployeeDto;
import hu.cubix.hr.patrik.dto.SalaryAvgDto;
import hu.cubix.hr.patrik.mapper.CompanyMapper;
import hu.cubix.hr.patrik.mapper.EmployeeMapper;
import hu.cubix.hr.patrik.model.Company;
import hu.cubix.hr.patrik.service.CompanyService;
import hu.cubix.hr.patrik.service.SalaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyRestController {

    @Autowired
    CompanyService companyService;

    @Autowired
    SalaryService salaryService;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping
    public List<CompanyDto> findAll(@RequestParam Optional<Boolean> full) {
        if (full.orElse(false)) {
            return companyMapper.companiesToDtos(companyService.findAllWithEmployees());
        } else {
            return companyMapper.companiesToSummaryDtos(companyService.findAll());
        }
    }

    @GetMapping("/{id}")
    public CompanyDto findById(@PathVariable long id, @RequestParam Optional<Boolean> isFull) {
        if (isFull.orElse(false)) {
            return companyMapper.companyToDto(companyService.findByIdWithEmployees(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        } else {
            return companyMapper.companyToSummaryDto(companyService.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        }
    }

    @PostMapping()
    public CompanyDto create(@RequestBody @Valid CompanyDto companyDto) {
        return getCompanyDtoOrThrow(
                companyService.create(companyMapper.dtoToCompany(companyDto)),
                HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public CompanyDto update(@PathVariable long id, @RequestBody @Valid CompanyDto companyDto) {
        companyDto.setId(id);
        return getCompanyDtoOrThrow(
                companyService.update(companyMapper.dtoToCompany(companyDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        companyService.delete(id);
    }

    @PostMapping("/{id}/employees")
    public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
        return getCompanyDtoOrThrow(
                companyService.addNewEmployee(id, employeeMapper.dtoToEmployee(employeeDto)));
    }

    @DeleteMapping("/{id}/employees/{employeeId}")
    public CompanyDto deleteEmployeeFromCompany(@PathVariable long id, @PathVariable long employeeId) {
        return getCompanyDtoOrThrow(
                companyService.deleteEmployeeFromCompany(id, employeeId));
    }

    @PutMapping("/{id}/employees")
    public CompanyDto replaceEmployees(@PathVariable long id, @RequestBody List<@Valid EmployeeDto> employeeDtos) {
        return getCompanyDtoOrThrow(
                companyService.replaceEmployees(id, employeeMapper.dtosToEmployees(employeeDtos)));
    }

    @GetMapping("/findByEmployeesSalaryGreaterThan")
    public List<CompanyDto> findByEmployeesSalaryGreaterThan(@RequestParam int salary) {
        return companyMapper.companiesToDtos(companyService.findByEmployeesSalaryGreaterThan(salary));
    }

    @GetMapping("/findByCountEmployeesGreaterThanEqual")
    public List<CompanyDto> findByCountEmployeesGreaterThanEqual(@RequestParam int count) {
        return companyMapper.companiesToDtos(companyService.findByCountEmployeesGreaterThanEqual(count));
    }

    @GetMapping("/getAverageSalaryByCompanyId/{id}")
    public List<SalaryAvgDto> getAverageSalaryByCompanyId(@PathVariable long id) {
        return companyService.getAverageSalaryByCompanyId(id);
    }

    @PutMapping("/{id}/raiseMinSalary/{positionName}/{minSalary}")
    public void raiseMinSalary(@PathVariable long id, @PathVariable String positionName, @PathVariable int minSalary) {
        salaryService.raiseMinSalary(positionName, minSalary, id);
    }

    private CompanyDto getCompanyDtoOrThrow(Company company) {
        return getCompanyDtoOrThrow(company, HttpStatus.NOT_FOUND);
    }

    private CompanyDto getCompanyDtoOrThrow(Company company, HttpStatus thrownStatus) {
        if (company == null) {
            throw new ResponseStatusException(thrownStatus);
        }
        return companyMapper.companyToDto(company);
    }
}

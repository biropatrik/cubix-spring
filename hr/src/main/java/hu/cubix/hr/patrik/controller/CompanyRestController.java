package hu.cubix.hr.patrik.controller;

import hu.cubix.hr.patrik.dto.CompanyDto;
import hu.cubix.hr.patrik.dto.EmployeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyRestController {

    private Map<Long, CompanyDto> companies = new HashMap<>();
    {
        companies.put(1L, new CompanyDto(1L, 2222, "SoftWare", "3300 Eger", null));
        companies.put(2L, new CompanyDto(2L, 2230, "SW2", "3300 Eger", null));
    }

    @GetMapping
    public List<CompanyDto> findAll(@RequestParam Optional<Boolean> full) {
        if (full.orElse(false)) {
            return new ArrayList<>(companies.values());
        } else {
            return companies.values().stream()
                    .map(this::mapCompanyWithoutEmployees)
                    .toList();
        }
    }

    @GetMapping("/{id}")
    public CompanyDto findById(@PathVariable long id, @RequestParam Optional<Boolean> full) {
        CompanyDto company = getCompanyOrThrow(id);
        if (full.orElse(false)) {
            return company;
        } else {
            return mapCompanyWithoutEmployees(company);
        }
    }

    @PostMapping()
    public CompanyDto create(@RequestBody CompanyDto company) {
        if (companies.containsKey(company.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        companies.put(company.getId(), company);
        return company;
    }

    @PutMapping("/{id}")
    public CompanyDto update(@PathVariable long id, @RequestBody CompanyDto company) {
        getCompanyOrThrow(id);
        company.setId(id);
        companies.put(id, company);
        return company;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        companies.remove(id);
    }

    @PostMapping("/{id}/employees")
    public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employee) {
        CompanyDto company = getCompanyOrThrow(id);
        company.getEmployees().add(employee);
        return company;
    }

    @DeleteMapping("/{id}/employees/{employeeId}")
    public CompanyDto deleteEmployeeFromCompany(@PathVariable long id, @PathVariable long employeeId) {
        CompanyDto company = getCompanyOrThrow(id);
        company.getEmployees().removeIf(emp -> emp.getId() == employeeId);
        return company;
    }

    @PutMapping("/{id}/employees")
    public CompanyDto replaceEmpoyees(@PathVariable long id, @RequestBody List<EmployeeDto> employees) {
        CompanyDto company = getCompanyOrThrow(id);
        company.setEmployees(employees);
        return company;
    }

    private CompanyDto mapCompanyWithoutEmployees(CompanyDto c) {
        return new CompanyDto(c.getId(), c.getRegistrationNumber(), c.getName(), c.getAddress(), null);
    }

    private CompanyDto getCompanyOrThrow(long id) {
        CompanyDto company = companies.get(id);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return  company;
    }
}

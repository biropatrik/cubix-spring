package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.dto.EmployeeDto;
import hu.cubix.hr.patrik.model.Company;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService {

    private Map<Long, Company> companies = new HashMap<>();

    @Override
    public Company create(Company company) {
        if (findById(company.getId()) != null) {
            return null;
        }
        return save(company);
    }

    @Override
    public Company update(Company company) {
        if (findById(company.getId()) == null) {
            return null;
        }
        return save(company);
    }

    @Override
    public Company save(Company company) {
        companies.put(company.getId(), company);
        return company;
    }

    @Override
    public List<Company> findAll(Optional<Boolean> isFull) {
        if (isFull.orElse(false)) {
            return new ArrayList<>(companies.values());
        } else {
            return companies.values().stream()
                    .map(this::mapCompanyWithoutEmployees)
                    .toList();
        }
    }

    @Override
    public Company findById(long id, Optional<Boolean> isFull) {
        Company company = companies.get(id);
        if (isFull.orElse(false)) {
            return company;
        } else {
            return mapCompanyWithoutEmployees(company);
        }
    }

    public Company findById(long id) {
        return findById(id, Optional.of(true));
    }

    @Override
    public void remove(long id) {
        companies.remove(id);
    }

    @Override
    public Company addNewEmployee(long id, EmployeeDto employeeDto) {
        Company company = findById(id);
        if (company == null) {
            return null;
        }
        company.getEmployees().add(employeeDto);
        return company;
    }

    @Override
    public Company deleteEmployeeFromCompany(long id, long employeeId) {
        Company company = findById(id);
        if (company == null) {
            return null;
        }
        company.getEmployees().removeIf(emp -> emp.getId() == employeeId);
        return company;
    }

    @Override
    public Company replaceEmployees(long id, List<EmployeeDto> employeeDtos) {
        Company company = findById(id);
        if (company == null) {
            return null;
        }
        company.setEmployees(employeeDtos);
        return company;
    }

    private Company mapCompanyWithoutEmployees(Company c) {
        return new Company(c.getId(), c.getRegistrationNumber(), c.getName(), c.getAddress(), null);
    }
}

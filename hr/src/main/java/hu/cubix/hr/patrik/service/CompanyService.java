package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.dto.EmployeeDto;
import hu.cubix.hr.patrik.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    public Company create(Company company);

    public Company update(Company company);

    public Company save(Company company);

    public List<Company> findAll(Optional<Boolean> isFull);

    public Company findById(long id, Optional<Boolean> isFull);

    public void remove(long id);

    public Company addNewEmployee(long id, EmployeeDto employeeDto);

    public Company deleteEmployeeFromCompany(long id, long employeeId);

    public Company replaceEmployees(long id, List<EmployeeDto> employeeDtos);
}

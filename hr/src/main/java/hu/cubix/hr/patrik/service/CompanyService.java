package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.dto.SalaryAvgDto;
import hu.cubix.hr.patrik.model.Company;
import hu.cubix.hr.patrik.model.Employee;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    public Company create(Company company);

    public Company update(Company company);

    public List<Company> findAll();

    public Optional<Company> findById(long id);

    public void delete(long id);

    public Company addNewEmployee(long id, Employee employee);

    public Company deleteEmployeeFromCompany(long id, long employeeId);

    public Company replaceEmployees(long id, List<Employee> employees);

    public List<Company> findByEmployeesSalaryGreaterThan(int salary);

    public List<Company> findByCountEmployeesGreaterThanEqual(Integer count);

    public List<SalaryAvgDto> getAverageSalaryByCompanyId(Long companyId);
 }

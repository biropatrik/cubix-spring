package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.dto.SalaryAvgDto;
import hu.cubix.hr.patrik.model.Company;
import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EmployeeService employeeService;

    @Override
    public Company create(Company company) {
        if (companyRepository.existsById(company.getId())) {
            return null;
        }
        companyRepository.save(company);
        company.getEmployees().forEach(e -> {
            employeeService.save(e);
        });
        return company;
    }

    @Override
    public Company update(Company company) {
        if (!companyRepository.existsById(company.getId())) {
            return null;
        }
        replaceEmployees(company.getId(), company.getEmployees());
        return companyRepository.save(company);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> findById(long id) {
        return companyRepository.findById(id);
    }

    @Override
    public void delete(long id) {
        Company company = companyRepository.findById(id).get();
        company.getEmployees().forEach(e -> e.setCompany(null));
        company.getEmployees().clear();
        companyRepository.deleteById(id);
    }

    @Override
    public Company addNewEmployee(long id, Employee employee) {
        Company company = companyRepository.findById(id).get();
        company.addEmployee(employee);
        employeeService.save(employee);
        return company;
    }

    @Override
    public Company deleteEmployeeFromCompany(long id, long employeeId) {
        Company company = companyRepository.findById(id).get();
        Employee employee = employeeService.findById(employeeId).get();
        employee.setCompany(null);
        company.getEmployees().remove(employee);
        employeeService.save(employee);
        return company;
    }

    @Override
    public Company replaceEmployees(long id, List<Employee> employees) {
        Company company = companyRepository.findById(id).get();
        company.getEmployees().forEach(e -> e.setCompany(null));
        company.getEmployees().clear();
        employees.forEach(e -> {
                company.addEmployee(e);
                employeeService.save(e);
        });
        return company;
    }

    @Override
    public List<Company> findByEmployeesSalaryGreaterThan(int salary) {
        return companyRepository.findByEmployeesSalaryGreaterThan(salary);
    }

    @Override
    public List<Company> findByCountEmployeesGreaterThanEqual(Integer count) {
        return companyRepository.findByCountEmployeesGreaterThanEqual(count);
    }

    @Override
    public List<SalaryAvgDto> getAverageSalaryByCompanyId(Long companyId) {
        return companyRepository.getAverageSalaryByCompanyId(companyId);
    }
}
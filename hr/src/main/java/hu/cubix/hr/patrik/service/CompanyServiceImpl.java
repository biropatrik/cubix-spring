package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Company;
import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.repository.CompanyRepository;
import hu.cubix.hr.patrik.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Company create(Company company) {
        if (companyRepository.existsById(company.getId())) {
            return null;
        }
        return companyRepository.save(company);
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
        employeeRepository.save(employee);
        return company;
    }

    @Override
    public Company deleteEmployeeFromCompany(long id, long employeeId) {
        Company company = companyRepository.findById(id).get();
        Employee employee = employeeRepository.findById(employeeId).get();
        employee.setCompany(null);
        company.getEmployees().remove(employee);
        employeeRepository.save(employee);
        return company;
    }

    @Override
    public Company replaceEmployees(long id, List<Employee> employees) {
        Company company = companyRepository.findById(id).get();
        company.getEmployees().forEach(e -> e.setCompany(null));
        company.getEmployees().clear();
        employees.forEach(e -> {
                company.addEmployee(e);
                employeeRepository.save(e);
        });
        return company;
    }
}

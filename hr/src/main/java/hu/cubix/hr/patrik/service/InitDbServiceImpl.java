package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.*;
import hu.cubix.hr.patrik.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InitDbServiceImpl implements InitDbService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    CompanyTypeRepository companyTypeRepository;

    @Autowired
    PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;

    @Override
    @Transactional
    public void clearDB() {
        employeeRepository.deleteAll();
        positionDetailsByCompanyRepository.deleteAll();
        positionRepository.deleteAll();
        companyRepository.deleteAll();
        companyTypeRepository.deleteAll();
    }

    @Override
    @Transactional
    public void insertTestData() {
        Position position1 = positionRepository.save(new Position("Java Developer", Qualification.UNIVERSITY));
        Position position2 = positionRepository.save(new Position("Sitebuild", Qualification.HIGH_SCHOOL));

        Employee emp1 = new Employee("John Doe", position1, 900, LocalDateTime.of(2021,4,11, 8, 0));
        Employee emp2 = new Employee("Carson Conrad", position1, 1200, LocalDateTime.of(2020,2,1, 8, 0));
        Employee emp3 = new Employee("Anakin Boone", position1, 1300, LocalDateTime.of(2022,6,10, 8, 0));
        Employee emp4 = new Employee("Allan Fox", position1, 1000, LocalDateTime.of(2019,5,10, 8, 0));
        Employee emp5 = new Employee("Allan Fox", position2, 1010, LocalDateTime.of(2019,5,10, 8, 0));
        Employee emp6 = new Employee("Allan Fox", position2, 10, LocalDateTime.of(2019,5,10, 8, 0));

        CompanyType typeLLC = companyTypeRepository.save(new CompanyType("LLC"));

        Company com1 = new Company(12345, "First Company", "3300 Eger", List.of(emp1), typeLLC);
        Company com2 = new Company(45678, "Test Company", "4000 Debrecen", List.of(emp2, emp3, emp4, emp5, emp6), typeLLC);

        companyRepository.saveAll(List.of(com1, com2));
        employeeRepository.saveAll(List.of(emp1, emp2, emp3, emp4, emp5, emp6));

        PositionDetailsByCompany pd = new PositionDetailsByCompany();
        pd.setCompany(com2);
        pd.setMinSalary(700);
        pd.setPosition(position1);
        positionDetailsByCompanyRepository.save(pd);

        PositionDetailsByCompany pd2 = new PositionDetailsByCompany();
        pd2.setCompany(com2);
        pd2.setMinSalary(500);
        pd2.setPosition(position2);
        positionDetailsByCompanyRepository.save(pd2);
    }
}

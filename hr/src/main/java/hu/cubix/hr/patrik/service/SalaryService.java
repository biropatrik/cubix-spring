package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.repository.EmployeeRepository;
import hu.cubix.hr.patrik.repository.PositionDetailsByCompanyRepository;
import hu.cubix.hr.patrik.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {

    private EmployeeService employeeService;
    private PositionRepository positionRepository;
    private PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
    private EmployeeRepository employeeRepository;

    public SalaryService(EmployeeService employeeService,
                         PositionRepository positionRepository,
                         PositionDetailsByCompanyRepository positionDetailsByCompanyRepository,
                         EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.positionRepository = positionRepository;
        this.positionDetailsByCompanyRepository = positionDetailsByCompanyRepository;
        this.employeeRepository = employeeRepository;
    }

    public void giveNewSalaryForEmployee(Employee employee) {
        employee.setSalary((int) (employee.getSalary() / 100f * (100 + employeeService.getPayRaisePercent(employee))));
    }

    @Transactional
    public void raiseMinSalary(String positionName, int minSalary, long companyId) {
        positionDetailsByCompanyRepository.findByPositionNameAndCompanyId(positionName, companyId)
                .forEach(pd -> {
                    pd.setMinSalary(minSalary);
                    /*
                    //1. Megoldás: sok külön UPDATE, kevésbé hatékony
                    pd.getCompany().getEmployees().forEach(e -> {
                        if (e.getPosition().equals(pd.getPosition()) && e.getSalary() < minSalary) {
                            e.setSalary(minSalary);
                        }
                    });
                     */

                    //2. Megoldás: bulk update
                    employeeRepository.updateSalaries(positionName, minSalary, companyId);
                });
    }
}

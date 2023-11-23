package hu.cubix.hr.patrik.repository;

import hu.cubix.hr.patrik.dto.SalaryAvgDto;
import hu.cubix.hr.patrik.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.employees WHERE c.id = ?1")
    Optional<Company> findByIdWithEmployees(long id);

    @Query("SELECT DISTINCT c FROM Company c LEFT JOIN FETCH c.employees")
    List<Company> findAllWithEmployees();

    @Query("SELECT DISTINCT c FROM Company c JOIN FETCH c.employees e WHERE e.salary > ?1")
    List<Company> findByEmployeesSalaryGreaterThan(Integer salary);

    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.employees WHERE SIZE(c.employees) > ?1")
    List<Company> findByCountEmployeesGreaterThanEqual(Integer count);

    @Query("SELECT new hu.cubix.hr.patrik.dto.SalaryAvgDto(e.position.name, AVG(e.salary))"
            + "FROM Employee e WHERE e.company.id = ?1 GROUP BY e.position.name ORDER BY AVG(e.salary) DESC")
    List<SalaryAvgDto> getAverageSalaryByCompanyId(Long companyId);
}

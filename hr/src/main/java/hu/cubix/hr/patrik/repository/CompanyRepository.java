package hu.cubix.hr.patrik.repository;

import hu.cubix.hr.patrik.dto.SalaryAvgDto;
import hu.cubix.hr.patrik.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByEmployeesSalaryGreaterThan(Integer salary);

    @Query("select c from Company c left join Employee e on c = e.company group by c.id having count(*) >= ?1")
    List<Company> findByCountEmployeesGreaterThanEqual(Integer count);

    @Query("select new hu.cubix.hr.patrik.dto.SalaryAvgDto(e.position.name, AVG(e.salary))"
            + "from Employee e where e.company.id = ?1 group by e.position.name order by AVG(e.salary) desc")
    List<SalaryAvgDto> getAverageSalaryByCompanyId(Long companyId);
}

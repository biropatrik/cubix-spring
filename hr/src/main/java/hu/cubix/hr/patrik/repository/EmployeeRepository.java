package hu.cubix.hr.patrik.repository;

import hu.cubix.hr.patrik.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @EntityGraph(attributePaths = {"manager", "managedEmployees"})
    Optional<Employee> findByUsername(String username);

    Page<Employee> findByPositionName(String positionName, Pageable pageable);

    List<Employee> findByNameStartingWithIgnoreCase(String name);

    List<Employee> findByEntryDateBetween(LocalDateTime from, LocalDateTime to);

    List<Employee> findBySalaryGreaterThan(Integer salary);

    @Query("update Employee e set e.salary = :minSalary " +
            "where e.position.name = :positionName " +
            "and e.salary < :minSalary " +
            "and e.company.id = :companyId")
    @Modifying
    @Transactional
    void updateSalaries(String positionName, int minSalary, long companyId);
}

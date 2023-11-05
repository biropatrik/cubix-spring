package hu.cubix.hr.patrik.repository;

import hu.cubix.hr.patrik.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByJob(String job);

    List<Employee> findByNameStartingWithIgnoreCase(String name);

    List<Employee> findByEntryDateBetween(LocalDateTime from, LocalDateTime to);

    List<Employee> findBySalaryGreaterThan(Integer salary);
}

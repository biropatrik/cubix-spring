package hu.cubix.hr.patrik.repository;

import hu.cubix.hr.patrik.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}

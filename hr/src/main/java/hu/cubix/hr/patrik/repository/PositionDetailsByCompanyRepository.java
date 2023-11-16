package hu.cubix.hr.patrik.repository;

import hu.cubix.hr.patrik.model.PositionDetailsByCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionDetailsByCompanyRepository extends JpaRepository<PositionDetailsByCompany, Long> {

    List<PositionDetailsByCompany> findByPositionNameAndCompanyId(String positionName, Long companyId);
}

package hu.cubix.hr.patrik.repository;

import hu.cubix.hr.patrik.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByName(String name);
}
package hu.cubix.logistics.patrik.repository;

import hu.cubix.logistics.patrik.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("SELECT s FROM Section s WHERE s.start.id = ?1 OR s.end.id = ?1")
    public Optional<Section> findByMilestoneId(long id);

    public Optional<Section> findByTransportPlanIdAndOrderOfSection(long id, short order);
}

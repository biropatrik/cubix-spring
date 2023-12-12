package hu.cubix.logistics.patrik.repository;

import hu.cubix.logistics.patrik.model.TransportPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransportPlanRepository extends JpaRepository<TransportPlan, Long> {

    @Query("SELECT t FROM TransportPlan t LEFT JOIN FETCH t.sections WHERE t.id = ?1")
    public Optional<TransportPlan> findByIdWithFetch(long id);

    @Query("SELECT COUNT(t) > 0 FROM TransportPlan t "+
           "LEFT JOIN t.sections s " +
           "WHERE t.id = ?1 " +
           "AND ( s.start.id = ?2 OR s.end.id = ?2 )")
    public boolean doesMilestoneExistsInTransportPlanByIds(long transportPlanId, long milestoneId);
}

package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Employee_;
import hu.cubix.hr.patrik.model.Vacation;
import hu.cubix.hr.patrik.model.VacationStatus;
import hu.cubix.hr.patrik.model.Vacation_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VacationSpecifications {

    public static Specification<Vacation> vacationStatusEquals(VacationStatus status) {
        return (root, cq, cb) -> cb.equal(root.get(Vacation_.status), status);
    }

    public static Specification<Vacation> requesterNameStartsWith(String prefix) {
        return (root, cq, cb) ->
                cb.like(cb.lower(root.get(Vacation_.requester).get(Employee_.name)), prefix.toLowerCase() + "%");
    }

    public static Specification<Vacation> managerNameStartsWith(String prefix) {
        return (root, cq, cb) ->
                cb.like(cb.lower(root.get(Vacation_.managerOfEmployee).get(Employee_.name)), prefix.toLowerCase() + "%");
    }

    public static Specification<Vacation> insertedTimeBetween(LocalDate from, LocalDate to) {
        return (root, cq, cb) -> cb.between(root.get(Vacation_.insertedTime).as(LocalDate.class), from, to);
    }

    public static Specification<Vacation> vacationIntervalIn(LocalDateTime from, LocalDateTime to) {
        return (root, cq, cb) -> cb.or(
                cb.between(root.get(Vacation_.startDate), from, to),
                cb.between(root.get(Vacation_.endDate), from, to));
    }
}

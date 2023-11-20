package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EmployeeSpecifications {

    public static Specification<Employee> hasId(long id) {
        return (root, cq, cb) -> cb.equal(root.get(Employee_.id), id);
    }

    public static Specification<Employee> nameStartsWith(String prefix) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get(Employee_.name)), prefix.toLowerCase() + "%");
    }

    public static Specification<Employee> positionNamesEquals(String positionName) {
        return (root, cq, cb) -> cb.equal(root.get(Employee_.position).get(Position_.name), positionName);
    }

    public static Specification<Employee> salaryBetween(int salaryAfter, int salaryBefore) {
        return (root, cq, cb) -> cb.between(root.get(Employee_.salary), salaryAfter, salaryBefore);
    }

    public static Specification<Employee> entryDateEquals(LocalDate localDate) {
        return (root, cq, cb) -> cb.equal(root.get(Employee_.entryDate).as(LocalDate.class), localDate);
    }

    public static Specification<Employee> companyNameStartsWith(String prefix) {
        return (root, cq, cb) ->
                cb.like(cb.lower(root.get(Employee_.company).get(Company_.name)), prefix.toLowerCase() + "%");
    }
}

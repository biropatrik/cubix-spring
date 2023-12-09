package hu.cubix.logistics.patrik.service;

import hu.cubix.logistics.patrik.model.Address;
import hu.cubix.logistics.patrik.model.Address_;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecifications {

    public static Specification<Address> cityStartsWith(String prefix) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get(Address_.city)), prefix.toLowerCase() + "%");
    }

    public static Specification<Address> streetStartsWith(String prefix) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get(Address_.street)), prefix.toLowerCase() + "%");
    }

    public static Specification<Address> countryISOEquals(String iso) {
        return (root, cq, cb) -> cb.equal(root.get(Address_.countryISO), iso);
    }

    public static Specification<Address> zipCodeEquals(String zipCode) {
        return (root, cq, cb) -> cb.equal(root.get(Address_.zipCode), zipCode);
    }
}

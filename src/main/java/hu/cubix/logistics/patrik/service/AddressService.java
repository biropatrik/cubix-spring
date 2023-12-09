package hu.cubix.logistics.patrik.service;

import hu.cubix.logistics.patrik.dto.AddressSearchDto;
import hu.cubix.logistics.patrik.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {

    public Address create(Address address);

    public List<Address> findAll();

    public Address findById(long id);

    public void deleteById(long id);

    public Address update(Address address);

    public Page<Address> findAddressesByExample(AddressSearchDto searchDto, Pageable pageable);
}

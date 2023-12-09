package hu.cubix.logistics.patrik.service;

import hu.cubix.logistics.patrik.dto.AddressSearchDto;
import hu.cubix.logistics.patrik.exception.AddressNotFoundException;
import hu.cubix.logistics.patrik.model.Address;
import hu.cubix.logistics.patrik.repository.AddressRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    @Transactional
    public Address create(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address findById(long id) {
        return addressRepository.findById(id)
                .orElseThrow(AddressNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        addressRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Address update(Address address) {
        if (!addressRepository.existsById(address.getId())) {
            throw new AddressNotFoundException();
        }
        return addressRepository.save(address);
    }

    @Override
    public Page<Address> findAddressesByExample(AddressSearchDto searchDto, Pageable pageable) {

        Specification<Address> specification = Specification.where(null);

        if (StringUtils.isNotBlank(searchDto.getCity())) {
            specification = specification.and(AddressSpecifications.cityStartsWith(searchDto.getCity()));
        }

        if (StringUtils.isNotBlank(searchDto.getStreet())) {
            specification = specification.and(AddressSpecifications.streetStartsWith(searchDto.getStreet()));
        }

        if (StringUtils.isNotBlank(searchDto.getCountryISO())) {
            specification = specification.and(AddressSpecifications.countryISOEquals(searchDto.getCountryISO()));
        }

        if (StringUtils.isNotBlank(searchDto.getZipCode())) {
            specification = specification.and(AddressSpecifications.zipCodeEquals(searchDto.getZipCode()));
        }

        return addressRepository.findAll(specification, pageable);
    }
}

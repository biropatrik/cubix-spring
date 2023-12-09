package hu.cubix.logistics.patrik.controller;

import hu.cubix.logistics.patrik.dto.AddressDto;
import hu.cubix.logistics.patrik.dto.AddressSearchDto;
import hu.cubix.logistics.patrik.exception.DifferentAddressIdsException;
import hu.cubix.logistics.patrik.mapper.AddressMapper;
import hu.cubix.logistics.patrik.model.Address_;
import hu.cubix.logistics.patrik.service.AddressService;
import hu.cubix.logistics.patrik.validation.Create;
import hu.cubix.logistics.patrik.validation.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressRestController {

    @Autowired
    AddressService addressService;

    @Autowired
    AddressMapper addressMapper;

    @PostMapping
    public AddressDto create(@RequestBody @Validated({Create.class}) AddressDto addressDto) {
        return addressMapper.addressToDto(
                    addressService.create(
                        addressMapper.dtoToAddress(addressDto)));
    }

    @GetMapping
    public List<AddressDto> findAll() {
        return addressMapper.addressesToDtos(addressService.findAll());
    }

    @GetMapping("/{id}")
    public AddressDto findById(@PathVariable long id) {
        return addressMapper.addressToDto(
                addressService.findById(id));
    }

    @PutMapping("/{id}")
    public AddressDto update(@PathVariable long id, @RequestBody @Validated({Update.class}) AddressDto addressDto) {
        if (addressDto.getId() != null && addressDto.getId() != id) {
            throw new DifferentAddressIdsException();
        }

        addressDto.setId(id);
        return addressMapper.addressToDto(
                addressService.update(addressMapper.dtoToAddress(addressDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        addressService.deleteById(id);
    }

    @PostMapping("/search")
    public List<AddressDto> search(
            @RequestBody AddressSearchDto searchDto,
            @SortDefault(value = Address_.ID, direction = Sort.Direction.ASC) Pageable pageable) {
        return addressMapper.addressesToDtos(
                addressService.findAddressesByExample(searchDto, pageable).getContent());
    }
}

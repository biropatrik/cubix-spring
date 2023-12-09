package hu.cubix.logistics.patrik.mapper;

import hu.cubix.logistics.patrik.dto.AddressDto;
import hu.cubix.logistics.patrik.model.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    public AddressDto addressToDto(Address address);

    public Address dtoToAddress(AddressDto addressDto);

    public List<AddressDto> addressesToDtos(List<Address> addresses);
}

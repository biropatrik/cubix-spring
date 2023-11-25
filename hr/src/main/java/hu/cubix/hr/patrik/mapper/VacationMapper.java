package hu.cubix.hr.patrik.mapper;

import hu.cubix.hr.patrik.dto.EmployeeDto;
import hu.cubix.hr.patrik.dto.VacationDto;
import hu.cubix.hr.patrik.dto.VacationInsertDto;
import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.model.Vacation;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VacationMapper {

    public VacationDto vacationToDto(Vacation vacation);

    public List<VacationDto> vacationsToDtos(List<Vacation> vacations);

    @InheritInverseConfiguration
    public Vacation dtoToVacation(VacationDto vacationDto);

    public List<Vacation> dtosToVacations(List<VacationDto> vacationDtos);

    @Mapping(target = "requester.id", source = "requesterId")
    public Vacation insertDtoToVacation(VacationInsertDto vacationInsertDto);

    @Mapping(target = "positionName", source = "position.name")
    @Mapping(target = "company.employees", ignore = true)
    public EmployeeDto employeeToDto(Employee employee);

    @InheritInverseConfiguration
    public Employee dtoToEmployee(EmployeeDto employeeDto);
}

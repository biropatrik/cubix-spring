package hu.cubix.hr.patrik.mapper;

import hu.cubix.hr.patrik.dto.EmployeeDto;
import hu.cubix.hr.patrik.model.Employee;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "positionName", source = "position.name")
    @Mapping(target = "company.employees", ignore = true)
    public EmployeeDto employeeToDto(Employee employee);

    public List<EmployeeDto> employeesToDtos(List<Employee> employees);

    @InheritInverseConfiguration
    public Employee dtoToEmployee(EmployeeDto employeeDto);

    public List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
}

package hu.cubix.hr.patrik.mapper;

import hu.cubix.hr.patrik.dto.EmployeeDto;
import hu.cubix.hr.patrik.model.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    public EmployeeDto employeeToDto(Employee employee);

    public List<EmployeeDto> employeesToDtos(List<Employee> employees);

    public Employee dtoToEmployee(EmployeeDto employeeDto);

    public List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
}

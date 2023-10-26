package hu.cubix.hr.patrik.controller;

import hu.cubix.hr.patrik.dto.EmployeeDto;
import hu.cubix.hr.patrik.mapper.EmployeeMapper;
import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.service.AbstractEmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    @Autowired
    private AbstractEmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping
    public List<EmployeeDto> findAll() {
        return employeeMapper.employeesToDtos(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public EmployeeDto findById(@PathVariable Long id) {
        return getEmployeeDtoOrThrow(
                employeeService.findById(id));
    }

    @PostMapping
    public EmployeeDto create(@RequestBody @Valid EmployeeDto employeeDto) {
        return getEmployeeDtoOrThrow(
                employeeService.create(employeeMapper.dtoToEmployee(employeeDto)), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable Long id, @RequestBody @Valid EmployeeDto employeeDto) {
        employeeDto.setId(id);
        return getEmployeeDtoOrThrow(
                employeeService.update(employeeMapper.dtoToEmployee(employeeDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.remove(id);
    }

    @GetMapping("/findByHigherSalary/{salary}")
    public List<EmployeeDto> findByHigherSalary(@PathVariable int salary) {
        return employeeMapper.employeesToDtos(employeeService.findByHigherSalary(salary));
    }

    @PostMapping("/payRaise")
    public int getPayRaise(@RequestBody Employee employee) {
        return employeeService.getPayRaisePercent(employee);
    }

    private EmployeeDto getEmployeeDtoOrThrow(Employee employee) {
        return getEmployeeDtoOrThrow(employee, HttpStatus.NOT_FOUND);
    }

    private EmployeeDto getEmployeeDtoOrThrow(Employee employee, HttpStatus thrownStatus) {
        if (employee == null) {
            throw new ResponseStatusException(thrownStatus);
        }
        return employeeMapper.employeeToDto(employee);
    }
}

package hu.cubix.hr.patrik.controller;

import hu.cubix.hr.patrik.dto.EmployeeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private Map<Long, EmployeeDto> employees = new HashMap<>();

    {
        employees.put(1L, new EmployeeDto(1, "Java Developer", 350, LocalDateTime.now()));
    }

    @GetMapping
    public List<EmployeeDto> findAll() {
        return new ArrayList<>(employees.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> findById(@PathVariable Long id) {
        EmployeeDto employeeDto = employees.get(id);
        if(employeeDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto employeeDto) {
        if(employees.containsKey(employeeDto.getId())) {
            ResponseEntity.badRequest().build();
        }
        employees.put(employeeDto.getId(), employeeDto);
        return ResponseEntity.ok(employeeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        employeeDto.setId(id);
        if(!employees.containsKey(employeeDto.getId())) {
            return ResponseEntity.notFound().build();
        }
        employees.put(id, employeeDto);
        return ResponseEntity.ok(employeeDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employees.remove(id);
    }

    @GetMapping("/findByHigherSalary/{salary}")
    public List<EmployeeDto> findByHigherSalary(@PathVariable int salary) {
        List<EmployeeDto> employeesWithHigherSalary = new ArrayList<>();
        for(EmployeeDto employeeDto : employees.values()) {
            if(employeeDto.getSalary() > salary) {
                employeesWithHigherSalary.add(employeeDto);
            }
        }
        return employeesWithHigherSalary;
    }
}

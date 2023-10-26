package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.model.Employee;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmployeeService extends AbstractEmployeeService implements EmployeeService {

    @Override
    public int getPayRaisePercent(Employee employee) {
        return 5;
    }
}

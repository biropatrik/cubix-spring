package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.config.EmployeeConfigurationProperties;
import hu.cubix.hr.patrik.config.EmployeeConfigurationProperties.Salary.Raise;
import hu.cubix.hr.patrik.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmartEmployeeService implements EmployeeService {

    @Autowired
    private EmployeeConfigurationProperties config;

    @Override
    public int getPayRaisePercent(Employee employee) {
        int percentage = 0;
        float maxYear = 0;
        float yearsSpent = getYearsSpentFromDateTime(employee.getTimestamp());
        for(Raise raise : config.getSalary().getRaises()) {
            if(raise.getYears() >= maxYear && yearsSpent >= raise.getYears()) {
                percentage = raise.getPercentage();
                maxYear = raise.getYears();
            }
        }
        return percentage;
    }

    private float getYearsSpentFromDateTime(LocalDateTime dateTime) {
        return LocalDateTime.now().getYear() - dateTime.getYear();
    }
}

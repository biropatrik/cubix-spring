package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.config.EmployeeConfigurationProperties;
import hu.cubix.hr.patrik.config.EmployeeConfigurationProperties.Salary.Raise;
import hu.cubix.hr.patrik.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class SmartEmployeeService extends AbstractEmployeeService implements EmployeeService {

    @Autowired
    private EmployeeConfigurationProperties config;

    @Override
    public int getPayRaisePercent(Employee employee) {
        int percentage = 0;
        float maxYear = 0;
        float yearsSpent = getYearsSpentFromDateTime(employee.getEntryDate());
        for(Raise raise : config.getSalary().getRaises()) {
            if(raise.getYears() >= maxYear && yearsSpent >= raise.getYears()) {
                percentage = raise.getPercentage();
                maxYear = raise.getYears();
            }
        }
        return percentage;
    }

    private float getYearsSpentFromDateTime(LocalDateTime dateTime) {
        Period periodSpent = Period.between(dateTime.toLocalDate(), LocalDate.now());
        return periodSpent.getYears()
                + periodSpent.getMonths() / 12f
                + periodSpent.getDays() / (float) Year.of(periodSpent.getYears()).length();
    }
}

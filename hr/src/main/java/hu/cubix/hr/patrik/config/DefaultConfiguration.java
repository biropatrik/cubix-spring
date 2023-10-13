package hu.cubix.hr.patrik.config;

import hu.cubix.hr.patrik.service.DefaultEmployeeService;
import hu.cubix.hr.patrik.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!smart")
public class DefaultConfiguration {

    @Bean
    public EmployeeService employeeService() {
        return new DefaultEmployeeService();
    }
}

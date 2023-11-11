package hu.cubix.hr.patrik.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "employee")
@Component
public class EmployeeConfigurationProperties {

    private Salary salary;

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public static class Salary {

        private List<Raise> raises;

        public List<Raise> getRaises() {
            return raises;
        }

        public void setRaises(List<Raise> raises) {
            this.raises = raises;
        }

        public static class Raise {

            private float years;
            private int percentage;

            public float getYears() {
                return years;
            }

            public void setYears(float years) {
                this.years = years;
            }

            public int getPercentage() {
                return percentage;
            }

            public void setPercentage(int percentage) {
                this.percentage = percentage;
            }
        }
    }
}

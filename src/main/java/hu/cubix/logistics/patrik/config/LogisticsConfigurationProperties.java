package hu.cubix.logistics.patrik.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "logistics")
@Component
public class LogisticsConfigurationProperties {

    private Income income;

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public static class Income {

        List<Decrease> decrease;

        public List<Decrease> getDecrease() {
            return decrease;
        }

        public void setDecrease(List<Decrease> decrease) {
            this.decrease = decrease;
        }

        public static class Decrease {

            private int minutes;
            private int percentage;

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
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

package hu.cubix.hr.patrik.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class EmployeeDto {

    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String job;
    @Positive
    private int salary;
    @Past
    private LocalDateTime timestamp;

    public EmployeeDto()  {
    }

    public EmployeeDto(long id, String name, String job, int salary, LocalDateTime timestamp) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.salary = salary;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

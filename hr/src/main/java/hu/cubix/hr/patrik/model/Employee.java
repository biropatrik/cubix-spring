package hu.cubix.hr.patrik.model;

import java.time.LocalDateTime;

public class Employee {

    private long id;
    private String name;
    private String job;
    private int salary;
    private LocalDateTime timestamp;

    public Employee() {
    }

    public Employee(long id, String name, String job, int salary, LocalDateTime timestamp) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

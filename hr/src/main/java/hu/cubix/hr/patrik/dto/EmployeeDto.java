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
    private String positionName;
    @Positive
    private int salary;
    @Past
    private LocalDateTime entryDate;

    private CompanyDto company;

    public EmployeeDto()  {
    }

    public EmployeeDto(String name, String positionName, int salary, LocalDateTime entryDate) {
        this.name = name;
        this.positionName = positionName;
        this.salary = salary;
        this.entryDate = entryDate;
    }

    public EmployeeDto(long id, String name, String positionName, int salary, LocalDateTime entryDate) {
        this.id = id;
        this.name = name;
        this.positionName = positionName;
        this.salary = salary;
        this.entryDate = entryDate;
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

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }
}

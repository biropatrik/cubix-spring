package hu.cubix.hr.patrik.dto;

import hu.cubix.hr.patrik.model.CompanyType;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {

    private long id;
    private int registrationNumber;
    private String name;
    private String address;
    private List<@Valid EmployeeDto> employees = new ArrayList<>();
    private CompanyType companyType;

    public CompanyDto() {
    }

    public CompanyDto(long id, int registrationNumber, String name, String address, List<@Valid EmployeeDto> employees, CompanyType companyType) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.address = address;
        this.employees = employees;
        this.companyType = companyType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<EmployeeDto> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDto> employees) {
        this.employees = employees;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }
}

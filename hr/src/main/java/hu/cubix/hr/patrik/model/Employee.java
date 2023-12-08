package hu.cubix.hr.patrik.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Employee {

    @Id
    @GeneratedValue
    private long id;
    private String name;

    @ManyToOne
    private Position position;
    private int salary;
    private LocalDateTime entryDate;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "requester")
    private List<Vacation> vacations;

    @ManyToOne
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private List<Employee> managedEmployees;

    private String username;
    private String password;

    public Employee() {
    }

    public Employee(String name) {
        this.name = name;
    }

    public Employee(String name, Position position, int salary, LocalDateTime entryDate) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.entryDate = entryDate;
    }

    public Employee(long id, String name, Position position, int salary, LocalDateTime entryDate) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.entryDate = entryDate;
    }

    public Employee(long id, String name, Position position, int salary, LocalDateTime entryDate, Company company) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.entryDate = entryDate;
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Vacation> getVacations() {
        return vacations;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Employee> getManagedEmployees() {
        return managedEmployees;
    }

    public void setManagedEmployees(List<Employee> managedEmployees) {
        this.managedEmployees = managedEmployees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
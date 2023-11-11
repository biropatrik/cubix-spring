package hu.cubix.hr.patrik.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Objects;

@Entity
public class Position {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    @Enumerated
    private Qualification minimalQualification;

    @OneToMany(mappedBy = "position")
    private List<Employee> employees;

    public Position() {
    }

    public Position(String name, Qualification minimalQualification) {
        this.name = name;
        this.minimalQualification = minimalQualification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Qualification getMinimalQualification() {
        return minimalQualification;
    }

    public void setMinimalQualification(Qualification minimalQualification) {
        this.minimalQualification = minimalQualification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(id, position.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
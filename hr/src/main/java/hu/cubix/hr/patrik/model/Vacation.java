package hu.cubix.hr.patrik.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Vacation {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate;

    @NotNull
    @Future
    private LocalDateTime endDate;

    @NotNull
    private VacationStatus status;

    @NotNull
    @CreatedDate
    private LocalDateTime insertedTime;

    @NotNull
    @ManyToOne
    private Employee requester;

    @ManyToOne
    private Employee managerOfEmployee;

    @AssertTrue(message = "The end date must be greater than the start date!")
    private boolean isEndDateValid() {
        return !endDate.isBefore(startDate);
    }

    public Vacation() {
    }

    public Vacation(LocalDateTime startDate, LocalDateTime endDate, VacationStatus status, Employee requester) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.requester = requester;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public VacationStatus getStatus() {
        return status;
    }

    public void setStatus(VacationStatus status) {
        this.status = status;
    }

    public LocalDateTime getInsertedTime() {
        return insertedTime;
    }

    public void setInsertedTime(LocalDateTime insertedTime) {
        this.insertedTime = insertedTime;
    }

    public Employee getRequester() {
        return requester;
    }

    public void setRequester(Employee requester) {
        this.requester = requester;
    }

    public Employee getManagerOfEmployee() {
        return managerOfEmployee;
    }

    public void setManagerOfEmployee(Employee managerOfEmployee) {
        this.managerOfEmployee = managerOfEmployee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacation vacation = (Vacation) o;
        return id == vacation.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

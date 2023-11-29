package hu.cubix.hr.patrik.dto;

import hu.cubix.hr.patrik.model.VacationStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class VacationDto {

    private long id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate;

    @NotNull
    @Future
    private LocalDateTime endDate;

    @NotNull
    private VacationStatus status;

    private LocalDateTime insertedTime;

    private LocalDateTime approvedAt;

    private EmployeeDto requester;

    private EmployeeDto managerOfEmployee;

    @AssertTrue(message = "The end date must be greater than the start date!")
    private boolean isEndDateValid() {
        return !endDate.isBefore(startDate);
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

    public EmployeeDto getRequester() {
        return requester;
    }

    public void setRequester(EmployeeDto requester) {
        this.requester = requester;
    }

    public EmployeeDto getManagerOfEmployee() {
        return managerOfEmployee;
    }

    public void setManagerOfEmployee(EmployeeDto managerOfEmployee) {
        this.managerOfEmployee = managerOfEmployee;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
}

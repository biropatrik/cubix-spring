package hu.cubix.hr.patrik.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class VacationInsertDto {

    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate;

    @NotNull
    @Future
    private LocalDateTime endDate;

    @NotNull
    private long requesterId;

    @AssertTrue(message = "The end date must be greater than the start date!")
    private boolean isEndDateValid() {
        return !endDate.isBefore(startDate);
    }

    public VacationInsertDto() {
    }

    public VacationInsertDto(LocalDateTime startDate, LocalDateTime endDate, long requesterId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.requesterId = requesterId;
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

    public long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(long requesterId) {
        this.requesterId = requesterId;
    }
}

package hu.cubix.hr.patrik.dto;

import hu.cubix.hr.patrik.model.VacationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VacationExampleDto {

    private VacationStatus vacationStatus;

    private String requesterNamePrefix;

    private String managerNamePrefix;

    private LocalDate insertedDateFrom;

    private LocalDate insertedDateTo;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public VacationExampleDto(VacationStatus vacationStatus, String requesterNamePrefix, String managerNamePrefix,
                              LocalDate insertedDateFrom, LocalDate insertedDateTo, LocalDateTime startDate,
                              LocalDateTime endDate) {
        this.vacationStatus = vacationStatus;
        this.requesterNamePrefix = requesterNamePrefix;
        this.managerNamePrefix = managerNamePrefix;
        this.insertedDateFrom = insertedDateFrom;
        this.insertedDateTo = insertedDateTo;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public VacationStatus getVacationStatus() {
        return vacationStatus;
    }

    public void setVacationStatus(VacationStatus vacationStatus) {
        this.vacationStatus = vacationStatus;
    }

    public String getRequesterNamePrefix() {
        return requesterNamePrefix;
    }

    public void setRequesterNamePrefix(String requesterNamePrefix) {
        this.requesterNamePrefix = requesterNamePrefix;
    }

    public String getManagerNamePrefix() {
        return managerNamePrefix;
    }

    public void setManagerNamePrefix(String managerNamePrefix) {
        this.managerNamePrefix = managerNamePrefix;
    }

    public LocalDate getInsertedDateFrom() {
        return insertedDateFrom;
    }

    public void setInsertedDateFrom(LocalDate insertedDateFrom) {
        this.insertedDateFrom = insertedDateFrom;
    }

    public LocalDate getInsertedDateTo() {
        return insertedDateTo;
    }

    public void setInsertedDateTo(LocalDate insertedDateTo) {
        this.insertedDateTo = insertedDateTo;
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
}

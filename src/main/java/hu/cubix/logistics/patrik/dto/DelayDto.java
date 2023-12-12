package hu.cubix.logistics.patrik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class DelayDto {

    @NotNull
    @PositiveOrZero
    private Long milestoneId;

    @NotNull
    @Positive
    private Short delayInMinutes;

    public DelayDto() {
    }

    public DelayDto(Long milestoneId, Short delayInMinutes) {
        this.milestoneId = milestoneId;
        this.delayInMinutes = delayInMinutes;
    }

    public Long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public Short getDelayInMinutes() {
        return delayInMinutes;
    }

    public void setDelayInMinutes(Short delayInMinutes) {
        this.delayInMinutes = delayInMinutes;
    }
}

package hu.cubix.logistics.patrik.exception;

public class MilestoneIsNotInTheTransportPlanException extends RuntimeException {

    private static final String MESSAGE = "The milestone is not included in the current transport plan!";

    public MilestoneIsNotInTheTransportPlanException() {
        super(MESSAGE);
    }
}

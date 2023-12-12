package hu.cubix.logistics.patrik.exception;

public class MilestoneNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Milestone does not exists!";

    public MilestoneNotFoundException() {
        super(MESSAGE);
    }
}

package hu.cubix.hr.patrik.exception;

public class VacationAlreadyProcessedException extends RuntimeException {

    private static final String MESSAGE = "Vacation request already processed!";

    public VacationAlreadyProcessedException() {
        super(MESSAGE);
    }
}

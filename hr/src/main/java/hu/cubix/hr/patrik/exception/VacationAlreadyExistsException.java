package hu.cubix.hr.patrik.exception;

public class VacationAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "Vacation already exists!";

    public VacationAlreadyExistsException() {
        super(MESSAGE);
    }
}

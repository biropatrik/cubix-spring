package hu.cubix.hr.patrik.exception;

public class VacationNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Vacation not found!";

    public VacationNotFoundException() {
        super(MESSAGE);
    }
}
